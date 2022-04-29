package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Asiakas;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Myynti.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus ep�onnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Asiakas> listaaKaikki(){
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		System.out.println("Dao.listaaKaikki()");
		sql = "SELECT * FROM asiakkaat";    // P�ivitetty haku
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					while(rs.next()){
						Asiakas asiakas = new Asiakas();
						asiakas.setAsiakas_id(rs.getInt(1));	// Lis�tty asiakas_id
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));	
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(asiakkaat);
		return asiakkaat;
	}
	
	public ArrayList<Asiakas> listaaKaikki(String hakusana){
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		System.out.println("Dao.listaaKaikki(\"" + hakusana + "\")");
		
		// Poistettu sarakkeiden valinta sql-lauseesta 25.4.2022 /LS
		sql = "SELECT * FROM asiakkaat WHERE etunimi LIKE ? OR sukunimi LIKE ? OR puhelin LIKE ? OR sposti LIKE ?";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");
				stmtPrep.setString(3, "%" + hakusana + "%");
				stmtPrep.setString(4, "%" + hakusana + "%");
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui			
					while(rs.next()){
						Asiakas asiakas = new Asiakas();
						asiakas.setAsiakas_id(rs.getInt(1));	// Lis�tty Asiakas_id
						asiakas.setEtunimi(rs.getString(2));
						asiakas.setSukunimi(rs.getString(3));	
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return asiakkaat;
	}
	
	public boolean lisaaAsiakas(Asiakas asiakas) {
		boolean paluuArvo = true;
		System.out.println("Dao.lisaaAsiakas()");
		sql="INSERT INTO asiakkaat (etunimi, sukunimi, puhelin, sposti) VALUES(?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, asiakas.getEtunimi());
			stmtPrep.setString(2, asiakas.getSukunimi());
			stmtPrep.setString(3, asiakas.getPuhelin());
			stmtPrep.setString(4, asiakas.getSposti());
			stmtPrep.executeUpdate();
			// Jos haluaa tiet�� uusimman id:n
			// System.out.println("Uusin id on " + stmtPrep.getGeneratedKeys().getInt(1));
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
	
	// public boolean poistaAsiakas(String asiakas_id) {
	public boolean poistaAsiakas(int asiakas_id) {
		System.out.println("Dao.poistaAsiakas(" + asiakas_id + ")");
		// Oikeassa el�m�ss� tiedot ensisijaisesti merkit��n poistetuksi
		boolean paluuArvo = true;
		sql="DELETE FROM asiakkaat WHERE asiakas_id=?";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			// stmtPrep.setInt(1, Integer.parseInt(asiakas_id));
			stmtPrep.setInt(1, asiakas_id);
			stmtPrep.executeUpdate();
			con.close();
		} catch (SQLException e) { // Oli esimerkiss� Exception.
			e.printStackTrace();
			paluuArvo = false;
		}
		return paluuArvo;
	}
}
