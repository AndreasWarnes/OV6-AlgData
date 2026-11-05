import javax.xml.crypto.Data;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanDekomprimer {

  public static void main(String[] args) throws IOException {

    String innfil = args.length > 0 ? args[0] : "diverse.huff";
    String utfil = args.length > 1 ? args[1] : "diverse_ut.lyx";

    DataInputStream inn = new DataInputStream(new BufferedInputStream(new FileInputStream(innfil)));
    int originalLengde = inn.readInt();

    int[] frekvenser = new int[256];
    for(int verdi = 0; verdi < 256; verdi++) {
      frekvenser[verdi] = inn.readInt();
    }

    byte[] bits = inn.readAllBytes();
    inn.close();

    HuffmanNode rot = HuffmanTre.byggTre(frekvenser);
    byte[] resultat = new byte[originalLengde];

    int antallSkrevet = 0;
    HuffmanNode node = rot;

    for(int i = 0; i < bits.length && antallSkrevet < originalLengde; i++) {
      int enByte = bits[i] & 0xFF;
      for(int bitNr = 7; bitNr >= 0 && antallSkrevet < originalLengde; bitNr--) {
        int bit = (enByte >> bitNr) & 1;
        if(bit == 0) {
          node = node.venstre;
        } else {
          node = node.høyre;
        }
        if(node.erBlad()) {
          resultat[antallSkrevet] = (byte) node.verdi;
          antallSkrevet++;
          node = rot;
        }
      }
    }

    Files.write(Path.of(utfil),resultat);
    System.out.println(innfil + " → " + utfil + ": " + antallSkrevet + " bytes");
  }
}
