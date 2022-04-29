package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.Asiakas;
import model.dao.Dao;

@WebServlet("/asiakkaat/*")	// Endpoint
public class Asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Asiakkaat() {
        super();
        System.out.println("Asiakkaat.Asiakkaat()");
    }

    // Hae asiakas/asiakkaat
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		
		// Haetaan kutsun polkutiedot, esim. /asiakkaat/hakusana
		String pathInfo = request.getPathInfo();
		System.out.println("polku: " + pathInfo);
		String hakusana = "";
		if (pathInfo != null) {	// Korjattu 25.4.2022 /LS
			hakusana = pathInfo.replace("/", "");
		}

		// Daon m‰‰ritykset, korjattu 25.4.2022 /LS
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat = null;
		if (pathInfo != null) {
			asiakkaat = dao.listaaKaikki(hakusana);
		} else {
			asiakkaat = dao.listaaKaikki();
		}
		
		System.out.println(asiakkaat);

		// Muutetaan JSONiksi
		String strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
	}

	// Asiakkaan lis‰‰minen
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPost()");
		
		// Muutetaan kutsun mukana tuleva json-string json-objektiksi:
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		Asiakas asiakas = new Asiakas();
		asiakas.setEtunimi(jsonObj.getString("etunimi"));
		asiakas.setSukunimi(jsonObj.getString("sukunimi"));
		asiakas.setPuhelin(jsonObj.getString("puhelin"));
		asiakas.setSposti(jsonObj.getString("sposti"));
		System.out.println(asiakas);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		Dao dao = new Dao();
		if (dao.lisaaAsiakas(asiakas)) { // Metodi palauttaa true/false
			out.println("{\"response\":1}"); // Lis‰‰minen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Lis‰‰minen ep‰onnistui {"response":0}
		}
	}

	// Asiakkaan muuttaminen
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");
	}

	// Asiakkaan poisto
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doDelete()");
		
		String pathInfo = request.getPathInfo();  // Haetaan kutsun polkutiedot, esim. /ABC-123
		System.out.println("polku: " + pathInfo);
		//String poistettavaAsiakasid = pathInfo.replace("/", "");
		int asiakas_id = Integer.parseInt(pathInfo.replace("/", ""));

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		if (dao.poistaAsiakas(asiakas_id)) { // Metodi palauttaa true/false
			out.println("{\"response\":1}"); // Poistaminen onnistui {"response":1}
		} else {
			out.println("{\"response\":0}"); // Poistaminen ep‰onnistui {"response":0}
		}
	}

}
