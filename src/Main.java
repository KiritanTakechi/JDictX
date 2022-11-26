public class Main {
    public static void main(String[] args) {
        new Thread(JDict::new).start();
        System.out.println("程序已启动");
    }
}
