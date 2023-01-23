package br.com.unipix.api.NumeroService;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.ibm.icu.util.TimeZone;

import br.com.unipix.api.NumeroService.repository.CampanhaRepository;

@SpringBootApplication
@EnableScheduling
public class NumeroServiceApplication  {

	@Autowired
	CampanhaRepository campanhaRepository;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(NumeroServiceApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);

	}

	// @Override
	// public void run(String... args) throws IOException {

	// FileWriter arq = new FileWriter("numeros.txt");
	// PrintWriter gravarArq = new PrintWriter(arq);

	// for (int i = 0; i < 227; i++) {
	// System.out.println("Processando " + 100000 * (i + 1) + " de 22655855");
	// List<Campanha> campanha =
	// this.campanhaRepository.findAllLimit(PageRequest.of(i, 100000));
	// List<String> numeros = campanha.stream().filter(c ->
	// c.getSituacao().equals("Ativo"))
	// .map(c -> c.getNumero())
	// .collect(Collectors.toList());
	// String text = String.join("%n", numeros);
	// if(i > 0 ){
	// text = "%n" + text;
	// }
	// gravarArq.printf(text);

	// }

	// arq.close();

	// }

	// @Override
	// public void run(String... args) throws Exception {
	// URL whatismyip = new URL("http://checkip.amazonaws.com");
	// BufferedReader in = new BufferedReader(new InputStreamReader(
	// whatismyip.openStream()));

	// String ip = in.readLine(); // you get the IP as a String

	// webphone wobj = new webphone();
	// SIPNotifications sipnotifications = new SIPNotifications(wobj);
	// sipnotifications.Start();

	// wobj.API_SetParameter("loglevel", "0");
	// wobj.API_SetParameter("logtoconsole", "0");
	// wobj.API_SetParameter("serveraddress", "177.53.17.120:5060");
	// wobj.API_SetParameter("username", "55111185744186");
	// wobj.API_SetParameter("password", "W5UF6SpQajf6zR");
	// wobj.API_SetParameter("proxyaddress", ip);
	// wobj.API_SetParameter("register", "1");

	// wobj.API_Start();

	// System.out.println("Iniciando ...");
	// WaitForEnterKeyPress();
	// System.out.println("Iniciado");

	// SipCall sc1 = new SipCall(wobj, "5511948053050", 1);
	// SipCall sc2 = new SipCall(wobj, "5582999793235", 2);
	// SipCall sc3 = new SipCall(wobj, "5567981653622", 3);
	// SipCall sc4 = new SipCall(wobj, "5571993997753", 4);

	// sc1.start();
	// sc2.start();
	// sc3.start();
	// sc4.start();
	// try {
	// sc1.join();
	// sc2.join();
	// sc3.join();
	// sc4.join();
	// } catch (InterruptedException ex) {
	// ex.printStackTrace();
	// }

	// WaitForEnterKeyPress();
	// wobj.API_Unregister();
	// sipnotifications.Stop();
	// wobj.API_Stop();
	// System.exit(0);
	// }

	// static void WaitForEnterKeyPress() {
	// try {
	// // skip existing (old) input
	// int avail = System.in.available();
	// if (avail > 0)
	// System.in.read(new byte[avail]);
	// } catch (Exception e) {
	// }

	// try {
	// // wait for enter press
	// while (true) {
	// int ch = System.in.read();
	// if (ch == '\n')
	// break;
	// }
	// } catch (Exception e) {
	// }
	// }

}
