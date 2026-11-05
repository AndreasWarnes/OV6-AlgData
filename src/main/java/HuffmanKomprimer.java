import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanKomprimer {

  static int[] tellFrekvenser(byte[] data) {
    int[] frekvenser = new int[256];
    for (int i = 0; i < data.length; i++) {
      int verdi = data[i] & 0xFF;
      frekvenser[verdi]++;
    }
    return frekvenser;
  }

  public static void main(String[] args) throws IOException {
    String innfil = args.length > 0 ? args[0] : "diverse.lyx";
    String utfil = args.length > 1 ? args[1] : "diverse.huff";

    byte[] data = Files.readAllBytes(Path.of(innfil));
    int[] frekvenser = tellFrekvenser(data);

    HuffmanNode rot = HuffmanTre.byggTre(frekvenser);
    long[] koder = new long[256];
    int[] lengder = new int[256];
    HuffmanTre.lagKode(rot, 0, 0, koder, lengder);

    DataOutputStream ut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utfil)));
    ut.writeInt(data.length);
    for (int verdi = 0; verdi < 256; verdi++) {
      ut.writeInt(frekvenser[verdi]);
    }

    BitWriter bitSkriver = new BitWriter(ut);
    for (int i = 0; i < data.length; i++) {
      int verdi = data[i] & 0xFF;
      bitSkriver.writeBits(koder[verdi], lengder[verdi]);
    }
    bitSkriver.lukk();

    long komprimertStørrelse = Files.size(Path.of(utfil));
    System.out.println(innfil + ": " + data.length + " bytes");
    System.out.println(utfil + ": " + komprimertStørrelse + " bytes");
  }
}