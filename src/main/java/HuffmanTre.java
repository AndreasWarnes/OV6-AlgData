import java.util.PriorityQueue;

public class HuffmanTre {

  static HuffmanNode byggTre(int[] frekvenser) {
    PriorityQueue<HuffmanNode> kø = new PriorityQueue<>((a,b) -> a.frekvens - b.frekvens);

    for(int verdi = 0; verdi < 256; verdi++) {
      if(frekvenser[verdi] > 0) {
        kø.add(new HuffmanNode(verdi, frekvenser[verdi]));
      }
    }
    while(kø.size() > 1) {
      HuffmanNode minst = kø.poll();
      HuffmanNode nestminst = kø.poll();
      kø.add(new HuffmanNode(minst, nestminst));
    }
    return kø.poll();
  }

  static void lagKode(HuffmanNode node, long kode, int lengde, long[] koder, int[] lengder) {
    if(node.erBlad()) {
      koder[node.verdi] = kode;
      lengder[node.verdi] = lengde;
      return;
    }

    lagKode(node.venstre, kode << 1, lengde + 1, koder, lengder);
    lagKode(node.høyre, (kode << 1) | 1, lengde + 1, koder, lengder);
  }
}
