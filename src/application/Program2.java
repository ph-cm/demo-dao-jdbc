package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartamentDao;
import model.entities.Departament;

public class Program2 {
	public static void main (String[] args) {
		Scanner sc = new Scanner(System.in);
		DepartamentDao departmentDao = DaoFactory.createDepartmentDao();
		
		Departament newDepartament = new Departament(null , "Limpeza");
		departmentDao.insert(newDepartament);
		System.out.println("Inserted, id: " + newDepartament.getId());
		
		Departament dep = departmentDao.findById(1);
		System.out.println(dep);
		
		Departament dep2 = departmentDao.findById(1);
		dep2.setName("Drink");
		departmentDao.update(dep2);
		System.out.println("updated");
		
		List<Departament> list = departmentDao.findAll();
		for(Departament d : list) {
			System.out.println(d);
		}
		
		System.out.println("Enter Id to remove: ");
		int id = sc.nextInt();
		departmentDao.deleteById(id);
		System.out.println("Deleted");
		
		sc.close();
	}
}
