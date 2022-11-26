import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Script {
    private static final Logger log = Logger.getLogger(String.valueOf(JDict.class));

    public static String ReadCmdLine(String command){
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            //获取执行结果
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }


        } catch (Exception e) {
            log.log(Level.parse("error"),"执行命令失败，e:{}",e);
        }finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        //replaceAll("\\\\x", "%"),解决返回结果编码问题
        return result.toString().replaceAll("\\\\x", "%");
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                log.log(Level.parse("error"),"关闭连接失败，e:{}");
            }
        }

    }
}
