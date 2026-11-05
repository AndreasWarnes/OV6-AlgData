import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class LZKomprimer {

  static final int VINDU = 32767;
  static final int MAKS_LENGDE = 127;
  static final int MIN_LENGDE = 4;
  static final int MAKS_UKOMPRIMERT = 128;

  static void skrivUkomprimret(DataOutputStream ut, byte[] data, int start, int slutt) throws IOException {
    while (start < slutt) {
      int antall = Math.min(MAKS_UKOMPRIMERT, slutt - start);
      ut.writeByte(-antall);
      ut.write(data, start, antall);
      start += antall;
    }
  }

  public static void main(String[] args) throws IOException {
    String innfil = args.length > 0 ? args[0] : "diverse.lyx";
    String utfil = args.length > 1 ? args[1] : "diverse.lz";

    byte[] data = Files.readAllBytes(Path.of(innfil));
    DataOutputStream ut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utfil)));

    ut.writeInt(data.length);
    int ukomprimertStart = 0;
    int posisjon = 0;

    while (posisjon < data.length) {
      int besteLengde = 0;
      int besteAvstand = 0;
      int maksLengde = Math.min(MAKS_LENGDE, data.length - posisjon);
      int startSøk = Math.max(0, posisjon - VINDU);

      for (int kandidat = posisjon - 1; kandidat >= startSøk; kandidat--) {
        int lengde = 0;
        while (lengde < maksLengde && data[kandidat + lengde] == data[posisjon + lengde]) {
          lengde++;
        }
        if (lengde > besteLengde) {
          besteLengde = lengde;
          besteAvstand = posisjon - kandidat;

          if (besteLengde == maksLengde) break;
        }
      }
      if (besteLengde >= MIN_LENGDE) {
        skrivUkomprimret(ut, data, ukomprimertStart, posisjon);
        ut.writeByte(besteLengde);
        ut.writeShort(besteAvstand);
        posisjon += besteLengde;
        ukomprimertStart = posisjon;
      } else {
        posisjon++;
      }
    }
    skrivUkomprimret(ut, data, ukomprimertStart, data.length);
    ut.close();
    System.out.println(innfil + ": " + data.length + " bytes");
    System.out.println(utfil + ": " + Files.size(Path.of(utfil)) + " bytes");
  }


}

