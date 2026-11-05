import java.io.DataOutputStream;
import java.io.IOException;

public class BitWriter {

  DataOutputStream ut;
  int buffer = 0;
  int antallBits = 0;

  BitWriter(DataOutputStream ut) {
    this.ut = ut;
  }

  void writeBits(long kode, int lengde) throws IOException {
    for(int i = lengde - 1; i >= 0; i--) {
      int bit = (int) ((kode >> i) & 1);
      buffer = (buffer << 1) | bit;
      antallBits++;
      if(antallBits == 8) {
        ut.write(buffer);
        buffer = 0;
        antallBits = 0;
      }
    }
  }

  void lukk() throws IOException {
    if(antallBits > 0) {
      buffer = buffer << (8 - antallBits);
      ut.write(buffer);
    }
    ut.close();
  }
}
