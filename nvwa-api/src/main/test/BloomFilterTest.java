import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/6/9
 * Time: 10:16 下午
 * <p>
 * Description:
 */

public class BloomFilterTest {

    @Test
    public void bloom() {
        BloomFilter bf = BloomFilter.create(
                Funnels.stringFunnel(Charset.forName("utf-8")),
                100000, 0.0001);
        for (int i = 0; i < 100000; i++) {
            bf.put(String.valueOf(i));
        }

        int count = 0;
        for (int i = 0; i < 10000; i++) {
            boolean b = bf.mightContain("imooc" + i);
            if (b) {
                count++;
            }
        }
        System.out.println("误判率：" + count);
    }
}
