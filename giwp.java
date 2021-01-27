import java.text.ParseException; import java.io.BufferedInputStream;
import java.io.IOException; import java.io.InputStream;
import java.net.InetAddress; import java.net.UnknownHostException;
import java.util.StringTokenizer;
public class ObtenerDireccionMAC {
	public static String dameDireccionMac() throws IOException {
		String sistemaOperativo = System.getProperty("os.name");
		String mac="";
		try {
			if (sistemaOperativo.startsWith("Windows")) {
				mac=localizaDireMacPorIp(ejecutarIpConfig());
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return mac;
	}
	private final static String localizaDireMacPorIp(String textoRespuesta)
			throws ParseException {
				String direccionIp = "";
				try{
					direccionIp = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					System.out.println(e.getMessage());
				}
				StringTokenizer tokenizer = new StringTokenizer(textoRespuesta, "\n");
				String direccionMac = "";
				String anterior="";
				while (tokenizer.hasMoreTokens()) {
					String texto = tokenizer.nextToken().trim();
					if (texto.contains(direccionIp) && direccionMac != null) 
						return direccionMac;
					int posicion = texto.indexOf(":");
					if (posicion <= 0) continue;
					String direccionMacTemp = texto.substring(posicion + 1).trim();
					if (direccionMacTemp.length() == 17) {
						direccionMac = direccionMacTemp;
						if (anterior.contains("Virtual")) {
							direccionMac="";
							throw new ParseException("Disable the virtual network", 0);
						}
						continue;
					}
					anterior=texto;
				}
				return "";
			}
			private final static String ejecutarIpConfig() throws IOException {
				Process p = Runtime.getRuntime().exec("ipconfig /all");
				InputStream stdoutStream =
					new BufferedInputStream(p.getInputStream());
				StringBuffer texto = new StringBuffer();
				int i;
				do{
					i=stdoutStream.read();
					if (i != -1) texto.append((char) i);
				} while(i != -1);
				String outputText = texto.toString();
				stdoutStream.close();
				return outputText;
			}
			public static void main(String[] args) {
				try {
					System.out.println("Operating System : "
						+ System.getProperty("os.name"));
					System.out.println("IP address : "
						+ InetAddress.getLocalHost().getHostAddress());
					System.out.println("MAC address : " + dameDireccionMac());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
		}
}
