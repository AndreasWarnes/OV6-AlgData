import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LZDekomprimer {

  public static void main(String[] args) throws IOException {
    String innfil = args.length > 0 ? args[0] : "diverse.lz";
    String utfil = args.length > 1 ? args[1] : "diverse_ut.lyx";

    DataInputStream inn = new DataInputStream(new BufferedInputStream(new FileInputStream(innfil)));
    int originalLengde = inn.readInt();
    byte[] resultat = new byte[originalLengde];
    int posisjon = 0;

    while (posisjon < originalLengde) {
      byte header = inn.readByte();

      if (header < 0) {
        int antall = -header;
        inn.readFully(resultat, posisjon, antall);
        posisjon += antall;
      } else {
        int lengde = header;
        int avstand = inn.readShort();
        int kopierFra = posisjon - avstand;

        for (int i = 0; i < lengde; i++) {
          resultat[posisjon] = resultat[kopierFra + i];
          posisjon++;
        }
      }
    }
    inn.close();
    Files.write(Path.of(utfil), resultat);
    System.out.println(innfil + " -> " + utfil + ": " + posisjon + " bytes");
  }


}
