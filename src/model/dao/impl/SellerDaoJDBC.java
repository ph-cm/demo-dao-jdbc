package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Departament;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
				st.setString(1, obj.getName());
				st.setString(2, obj.getEmail());
				st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
				st.setDouble(4, obj.getBaseSalary());
				st.setInt(5, obj.getDepartament().getId());
				
				int rowsAffected = st.executeUpdate();
				
				if(rowsAffected > 0) {
					ResultSet rs = st.getGeneratedKeys();
					if(rs.next()) {
						int id = rs.getInt(1);
						obj.setId(id);
					}
					DB.closeResultSet(rs);
						
					
				}else {
					throw new DbException("ERRO! Nenhuma linha afetada");
				}
		}
		catch(SQLException e){
			throw new DbException (e.getMessage());
		}
		finally{
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?, ",
					Statement.RETURN_GENERATED_KEYS);
			
				st.setString(1, obj.getName());
				st.setString(2, obj.getEmail());
				st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
				st.setDouble(4, obj.getBaseSalary());
				st.setInt(5, obj.getDepartament().getId());
				st.setInt(6, obj.getId());
				
		}
		catch(SQLException e){
			throw new DbException (e.getMessage());
		}
		finally{
			DB.closeStatement(st);
		}
	}
	

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet  rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName"	
					+ "FROM seller INNER JOIN department"
					+ "ON seller.DepartmentId = department.Id"
					+ "WHERE seller.Id = ?");
			
			st.setInt(1 , id);
			rs = st.executeQuery();
			if(rs.next()) {
				Departament dep = instanciateDepartament(rs);
				Seller obj = instanciateSeller(rs , dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instanciateSeller(ResultSet rs , Departament dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartament(dep);
		return obj;
		
	}

	private Departament instanciateDepartament(ResultSet rs) throws SQLException {
		Departament dep = new Departament();
		dep.setId(rs.getInt("DepartamentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet  rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer , Departament> map = new HashMap<>();
			
			while(rs.next()) {
				Departament dep = map.get(rs.getInt("Departament"));
				
				if(dep == null) {
					dep = instanciateDepartament(rs);
					map.put(rs.getInt("DeparatamentId"), dep);
				}
				
				Seller obj = instanciateSeller(rs , dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartament(Departament departament) {
		PreparedStatement st = null;
		ResultSet  rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ?"
					+ "ORDER BY Name");
			
			st.setInt(1 , departament.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer , Departament> map = new HashMap<>();
			
			while(rs.next()) {
				Departament dep = map.get(rs.getInt("Departament"));
				
				if(dep == null) {
					dep = instanciateDepartament(rs);
					map.put(rs.getInt("DeparatamentId"), dep);
				}
				
				Seller obj = instanciateSeller(rs , dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
