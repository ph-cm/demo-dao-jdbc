package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		Departament departament = new Departament(2 , null);
		List<Seller> list = sellerDao.findByDepartament(departament);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		list = sellerDao.findAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
		
		Seller newSeller = new Seller(null , "Joao" , "Joao@gmail.com" , new Date() , 5000.0 , departament);
		sellerDao.insert(newSeller);
		System.out.println("Inserted id = " + newSeller.getId());
		
		seller = sellerDao.findById(1);
		seller.setName("Jhon");
		sellerDao.update(seller);
		System.out.println("Updated");
		
		System.out.println("Digite um id para ser excluido: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Deleted");
	}
}
