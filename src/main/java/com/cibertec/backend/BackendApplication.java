package com.cibertec.backend;

import com.cibertec.backend.entites.*;
import com.cibertec.backend.repositories.*;
import com.cibertec.backend.utils.enums.ERole;
import com.cibertec.backend.utils.enums.EStateChangeDoc;
import com.cibertec.backend.utils.enums.EStateProduct;
import com.cibertec.backend.utils.enums.ETicketState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.plaf.metal.MetalIconFactory;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	private StateProductRepository stateProductRepository;

	@Autowired
	private StateChangeDocRepository stateChangeDocRepository;

	@Autowired
	private RoleUserAppWebRepository roleUserAppWebRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private  CPERepository cpeRepository;

	@Autowired
	private CPEDetailRepository cpeDetailRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ChangeDocRepository changeDocRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	private IUserAppWebRepository iUserAppWebRepository;

	@Bean
	CommandLineRunner init(){
		return args->{
			//saveRoles();
			saveStateProducts();
			saveStateChangeDoc();
			saveProducts();
			saveCustomer();
			saveCPE();//se registran comprobantes de hace 3, 12 y 13 meses. 1 CPE tiene 2 IMEI's

			String sale="vendedor";
			String tech="tecnico";

			if(!iUserAppWebRepository.findByName(sale).isPresent())
			{
				Set<String> strings=new HashSet<>();
				strings.add(String.valueOf(ERole.SALE));
				Set<RoleUserAppWeb> roleEntitySet=strings.stream()
						.map(role ->RoleUserAppWeb.builder()
								.name(ERole.valueOf(role))
								.build()).collect(Collectors.toSet());
				UserAppWeb userToaEntity=UserAppWeb.builder()
						.name(sale)
						.password(passwordEncoder.encode("1234"))
						.roles(roleEntitySet).
						build();
				iUserAppWebRepository.save(userToaEntity);
			}



			if(!iUserAppWebRepository.findByName(tech).isPresent())
			{
				Set<String> strings2=new HashSet<>();
				strings2.add(String.valueOf(ERole.TECH));
				Set<RoleUserAppWeb> roleEntitySet2=strings2.stream()
						.map(role ->RoleUserAppWeb.builder()
								.name(ERole.valueOf(role))
								.build()).collect(Collectors.toSet());


				UserAppWeb userToaEntity2=UserAppWeb.builder()
						.name(tech)
						.password(passwordEncoder.encode("1234"))
						.roles(roleEntitySet2).
						build();

				iUserAppWebRepository.save(userToaEntity2);
			}


			//LÓGICA
			/*

			// IMEI 490154203237518 tiene 2 modelos 	APPLE IPHONE12,
			//sirve para pruebas en las que el equipo será reemplazado por un
			//teléfono de la misma marca y modelo.

			//IMEI 353981107891745 SAMSUNG genera estado RECHAZADO de ticket, más allá de 12 meses


			//IMEI 860306033442129 cumple con garantía GOOGLE PIXEL
			// para pruebas en caso no se encuentre marca y modelo, deber{a listar el ONE PLUS que
			//se acerca al 10% - VENDIDO EN COMPROBANTE DE 2 ÍTEMS


			String imei="353981107891745";
			String comment="Cliente frecuente";
			boolean continuar=true;

			if(ticketRepository.findByProductImei(imei).isEmpty()){
				Ticket ticket=registrarTicket(imei,comment);//******MÉTODOO UNIFICADO CON  registraDocumentoCambio(), VER ChangeDocServiceImpl
				//ticket.getProduct()//throw error FETCH.EAGER
				if(ticket.getId()==null){
					continuar=false;
					System.out.println("IMEI NO EXISTE");

				}else{
					if(ticket.getETicketState().equals(ETicketState.APROBADO)){
						System.out.println("IMEMI CUMPLE CON GARANTÍA");

						registraDocumentoCambio(ticket.getId());//**MÉTODOO UNIFICADO registrarTicket

					}	else {
						System.out.println("IMEI NO CUMPLE CON GARANTÍA");
						continuar=false;
					}
				}
			}

			if(continuar){
				actualizarEstadoTecnicoRevision(imei);//**
				actualizarEstadoTecnicoAprobado(imei);//**

				List<Product> products=buscarProductoReemplazoMismaModeloMismaMarca(imei);//**
				if (products.size()>0){
					System.out.println("existe modelo y marcar para reemplazar");
					for (int i = 0; i < products.size(); i++) {
						System.out.println("modelo disponible");
						System.out.println(products.get(i).getBrand());
						System.out.println(products.get(i).getModel());
					}

//registrar solo 1, para pruebas.
					System.out.println("registrando cambio");
					registrarCambio(imei,products.get(0).getImei());//**

				}else{
					System.out.println("no existe la marca y modelo buscada");
					System.out.println("buscando productos de hasta 110% del precio de lista");
					List<Product> products1=buscarProductoReemplazoDiezPorCiento(imei);//**

					if (products1.size()>0){
						for (int i = 0; i < products1.size(); i++) {
							System.out.println("modelo disponible");
							System.out.println(products1.get(i).getBrand());
							System.out.println(products1.get(i).getModel());
						}

						//registrar solo 1, para pruebas.
						System.out.println("registrando cambio");
						registrarCambio(imei,products1.get(0).getImei());
					}else {
						System.out.println("no se encontraron productos disponibles cercanos al 110%");
					}


				}
			}
			 */





		};
	}




	public void saveRoles(){
		//ROLES


		for (int i = 0; i < ERole.values().length; i++) {

			ERole state= Arrays.stream(ERole.values()).collect(Collectors.toList()).get(i);

			if (roleUserAppWebRepository.findByName(state).isEmpty()) {
				RoleUserAppWeb stateProduct =  RoleUserAppWeb.builder()
						.name(state)
						.build();
				roleUserAppWebRepository.save(stateProduct);
			}
		}
	}
	public  void saveStateChangeDoc(){
		//Estados documento de cambio:
		for (int i = 0; i < EStateChangeDoc.values().length; i++) {

			EStateChangeDoc state= Arrays.stream(EStateChangeDoc.values()).collect(Collectors.toList()).get(i);
			String formattedNumber = String.format("%02d", i+1);

			if (stateChangeDocRepository.findByState(state).isEmpty()) {
				StateChangeDoc stateProduct =  StateChangeDoc.builder().id(formattedNumber)
						.state(state)
						.build();
				stateChangeDocRepository.save(stateProduct);
			}
		}
	}
	public  void saveStateProducts(){

		for (int i = 0; i < EStateProduct.values().length; i++) {

			EStateProduct state= Arrays.stream(EStateProduct.values()).collect(Collectors.toList()).get(i);
			String formattedNumber = String.format("%02d", i+1);

			if (stateProductRepository.findByState(state).isEmpty()) {
				StateProduct stateProduct =  StateProduct.builder().id(formattedNumber)
						.state(state)
						.build();
				stateProductRepository.save(stateProduct);
			}
		}
	}
	public  void saveProducts(){
		//PRODUCTOS
		StateProduct stateProductNew = stateProductRepository.findByState(EStateProduct.NUEVO).get();

		StateProduct stateProductSell = stateProductRepository.findByState(EStateProduct.VENDIDO).get();

		List<Product> products=Arrays.asList(Product.builder()
						.imei("353981107891745").model("S21").brand("SAMSUNG")
						.stateProduct(stateProductSell).price(4500.00)
						.build(),
				Product.builder()
						.imei("490154203237518")
						.model("iPhone 12")
						.brand("APPLE")
						.stateProduct(stateProductSell).price(5500.00)//reemplazar por APPLE
						.build(),
				Product.builder()
						.imei("456154203237223")
						.model("iPhone 12")
						.brand("APPLE")
						.stateProduct(stateProductNew).price(5500.00)//reemplazar por APPLE
						.build(),
				Product.builder()
						.imei("860306033442129")
						.model("Pixel 5")
						.brand("GOOGLE")
						.stateProduct(stateProductSell).price(5000.00)//SE REEMPLAZA POR ONEPLUS
						.build(),
				Product.builder()
						.imei("356938035643809")
						.model("OnePlus 9")
						.brand("ONEPLUS")
						.stateProduct(stateProductNew).price(5100.00)//SE APROXIMA A PIXEL 5 10%
						.build(),
				Product.builder()
						.imei("353981105498523")
						.model("Xperia 1 II")
						.brand("SONY")
						.stateProduct(stateProductSell).price(2400.00)
						.build(),Product.builder()
						.imei("490154203237537")
						.model("Mi 11")
						.brand("XIAOMI")
						.stateProduct(stateProductNew).price(1500.00)
						.build()
		);

		for (Product product
				:products) {
			if(productRepository.findById(product.getImei()).isEmpty()){
				productRepository.save(product);
			}

		}


	}

	public void saveCustomer(){


		List<Customer> customers=Arrays.asList(

				Customer.builder()
						.numDoc("12345678")
						.name("Juan Pérez")
						.phone("987654321")
						.mail("juan.perez@ejemplo.com")
						.build(),
				Customer.builder()
						.numDoc("23456789")
						.name("María García")
						.phone("987654322")
						.mail("maria.garcia@ejemplo.com")
						.build(),
				Customer.builder()
						.numDoc("34567890")
						.name("Carlos López")
						.phone("987654323")
						.mail("carlos.lopez@ejemplo.com")
						.build(),
				Customer.builder()
						.numDoc("78901234")
						.name("Ana Martínez")
						.phone("987654324")
						.mail("ana.martinez@ejemplo.com")
						.build(),
				Customer.builder()
						.numDoc("46712345")
						.name("Luis Rodríguez")
						.phone("987654325")
						.mail("luis.rodriguez@ejemplo.com")
						.build()

		);

		for (Customer customer:customers) {
			if(customerRepository.findByNumDoc(customer.getNumDoc()).isEmpty()){
				customerRepository.save(customer);
			}
		}

	}

	public void saveCPE(){

		//CPE de hace 3,12 y 13 meses
		//CPE con 1 y 2 detalles

		LocalDateTime threeMonthsAgo = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
		Timestamp timestampThreeMonthsAgo = Timestamp.valueOf(threeMonthsAgo);


		LocalDateTime oneYearAgo = LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
		Timestamp timestampOneYearAgo = Timestamp.valueOf(oneYearAgo);

		LocalDateTime moreThanOneYearAgo = LocalDateTime.now().minus(13, ChronoUnit.MONTHS);
		Timestamp timestampMoreThanOneYearAgo = Timestamp.valueOf(moreThanOneYearAgo);

		//lista de comprobantes
		List<CPE> cpes=Arrays.asList(
				CPE.builder()
						.typeCpe("03")
						.serie("BV01")
						.correlative("00012345")
						.date(timestampThreeMonthsAgo)
						.build(),
				CPE.builder()
						.typeCpe("03")
						.serie("B002")
						.correlative("00023456")
						.date(timestampMoreThanOneYearAgo)
						.build(),
				CPE.builder()
						.typeCpe("03")
						.serie("BV03")
						.correlative("00034567")
						.date(timestampOneYearAgo)
						.build()
		);


		//SOLO SE FILTRAN  PRODUCTOS VENDIDOS
		List<Product> products=productRepository.findByStateProductState(EStateProduct.VENDIDO);

		for (int i = 0; i < cpes.size();i++) {

			CPE cpe=cpes.get(i);

			if(cpeRepository.findByTypeCpeAndSerieAndCorrelative(cpe.getTypeCpe(),cpe.getSerie(),cpe.getCorrelative()).isEmpty()){

				Customer customer=customerRepository.findById(Long.valueOf(i+1)).get();
				cpe.setCustomer(customer);
				cpe=cpeRepository.save(cpe);
				Product product=products.get(i);

				CPEDetail cpeDetail1_1=CPEDetail.builder()
						.id(new CPEDetailId(product.getImei(), null))
						.product(product)
						.cpe(cpe)
						.unitPrice(product.getPrice()+1050)
						.build();

				cpeDetailRepository.save(cpeDetail1_1);

				if(i==cpes.size()-1){//2 productos

					Product additionalProduct = products.get(i + 1);

					//ítem adicional
					CPEDetail cpeDetail1_2=CPEDetail.builder()
							.id(new CPEDetailId(additionalProduct.getImei(), null))
							.product(additionalProduct)
							.cpe(cpe)
							.unitPrice(additionalProduct.getPrice()+1050)
							.build();
					cpeDetailRepository.save(cpeDetail1_2);
				}

			}

		}
	}

	public Ticket registrarTicket(String imei,String comment){

		//consultar si IMEI existe
		// si existe, verificar si  esta vendido o reemplazado,

		Optional<Product> product= productRepository.findProductsByImeiAndState(imei);
		if(product.isPresent()){

			//si cumple, consultar si está asociado a un comprobante o documento de cambio
			Optional<CPEDetail> cpeDetailIds = cpeDetailRepository.findByProductImei(imei);
			Optional<ChangeDoc> changeDocIds = changeDocRepository.findByProductImei(imei);

			if (cpeDetailIds.isEmpty() && changeDocIds.isEmpty()) {

				return new Ticket();//"IMEI NO EXISTE"
			}else{

				if(!changeDocIds.isEmpty()){//si existe IMEI EN DOCUMENTO DE CAMBIO

					LocalDate dateChangeDoc = changeDocIds.get().getDateLastState().toLocalDateTime().toLocalDate();//fecha último estado de documento de cambio
					LocalDate now = LocalDateTime.now().toLocalDate();
					LocalDate oneYearAgo = now.minus(1, ChronoUnit.YEARS);

					if(dateChangeDoc.isBefore(oneYearAgo)){

						return ticketRepository.save(
								Ticket.builder()
										.date(Timestamp.valueOf(LocalDateTime.now()))
										.customers(changeDocIds.get().getTicket().getCustomers())
										.product(changeDocIds.get().getProduct())
										.comments(comment)
										.eTicketState(ETicketState.RECHAZADO)
										.build()
						);						 //IMEI FUERA DE GARANTÍA

					}else{

						return ticketRepository.save(
								Ticket.builder()
										.date(Timestamp.valueOf(LocalDateTime.now()))
										.customers(changeDocIds.get().getTicket().getCustomers())
										.product(changeDocIds.get().getProduct())
										.comments(comment)
										.eTicketState(ETicketState.APROBADO)
										.build()
						);//IMEI DENTRO DE GARANTÍA
					}

				}else{

					LocalDate dateCpe=cpeDetailIds.get().getCpe().getDate().toLocalDateTime().toLocalDate();

					LocalDate now = LocalDateTime.now().toLocalDate();
					LocalDate oneYearAgo = now.minus(1, ChronoUnit.YEARS);

					if(dateCpe.isBefore(oneYearAgo)){

						return ticketRepository.save(
								Ticket.builder()
										.date(Timestamp.valueOf(LocalDateTime.now()))
										.customers(cpeDetailIds.get().getCpe().getCustomer())
										.product(cpeDetailIds.get().getProduct())
										.comments(comment)
										.eTicketState(ETicketState.RECHAZADO)
										.build()
						);//IMEI FUERA DE GARANTÍA

					}else{

						return ticketRepository.save(
								Ticket.builder()
										.date(Timestamp.valueOf(LocalDateTime.now()))
										.customers(cpeDetailIds.get().getCpe().getCustomer())
										.product(cpeDetailIds.get().getProduct())
										.comments(comment)
										.eTicketState(ETicketState.APROBADO)
										.build()
						);//IMEI DENTRO DE GARANTÍA

					}

				}

			}

		}else{

			return new Ticket();//"IMEI NO EXISTE";

		}

	}

	public void registraDocumentoCambio(Long ticketId){

		Ticket ticket= ticketRepository.findById(ticketId).get();
		StateChangeDoc stateChangeDoc= stateChangeDocRepository.findByState(EStateChangeDoc.GENERADO).get();

		changeDocRepository.save(
				ChangeDoc.builder()
						.date(Timestamp.valueOf(LocalDateTime.now()))
						.stateChangeDoc(stateChangeDoc)
						.ticket(ticket)
						.dateLastState(Timestamp.valueOf(LocalDateTime.now()))
						.build()
		);

	}


	public boolean actualizarEstadoTecnicoRevision(String imei){


		//buscar por imei, esto debe retornar la solicitud de cambio
		ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();
		changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.REVISION).get());
		changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
		changeDocRepository.save(changeDoc);

		Product product=productRepository.findById(imei).get();
		product.setStateProduct(stateProductRepository.findByState(EStateProduct.EVALUACION).get());
		productRepository.save(product);

		return true;
	}

	public boolean actualizarEstadoTecnicoAprobado(String imei){

		//buscar por imei, esto debe retornar la solicitud de cambio
		ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();
		changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.APROBADO).get());
		changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
		changeDocRepository.save(changeDoc);

		//actualizar estado del IMEI averiado

		Product product=productRepository.findById(imei).get();
		product.setStateProduct(stateProductRepository.findByState(EStateProduct.DEFECTUOSO).get());
		productRepository.save(product);

		return true;
	}

	public List<Product> buscarProductoReemplazoMismaModeloMismaMarca(String imei){
		System.out.println("tramo1");
		Product product= productRepository.findById(imei).get();
		System.out.println("tramo2");

		//se requiere misma marca, modelo y estado NUEVO
		List<Product> products= productRepository.findByBrandAndModelAndStateProduct(product.getBrand(),product.getModel(),stateProductRepository.findByState(EStateProduct.NUEVO).get());
		return products;
	}

	public boolean registrarCambio(String oldImei,String newImei){

		ChangeDoc changeDoc= ticketRepository.findByProductImei(oldImei).get().getChangeDoc();
		changeDoc.setProduct(productRepository.findById(newImei).get());//imei entregado
		changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
		changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.EJECUTADO).get());
		changeDocRepository.save(changeDoc);

		Product product=productRepository.findById(newImei).get();
		product.setStateProduct(stateProductRepository.findByState(EStateProduct.REEMPLAZO).get());
		productRepository.save(product);

		return true;
	}

	public boolean actualizarEstadoTecnicoRechazado(String imei){

		//buscar por imei, esto debe retornar la solicitud de cambio
		ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();
		changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.RECHAZADO).get());
		changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
		changeDocRepository.save(changeDoc);

		return true;
	}


	public boolean actualizarEstadoClienteEsperaLlegadaNuevoEquipo(String imei){

		//buscar por imei, esto debe retornar la solicitud de cambio
		ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();
		changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.ESPERA).get());
		changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
		changeDocRepository.save(changeDoc);

		return true;
	}

	public List<Product> buscarProductoReemplazoDiezPorCiento(String imei){

		Product product= productRepository.findById(imei).get();

		//se requiere misma marca, modelo y estado NUEVO
		List<Product> products= productRepository.findNewProductsUnder110PercentOfPriceByImeiAndStateProductNew(product.getImei());
		return products;
	}


}
