import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class disk {
    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("1.登录 2.注册 3.退出");
            int opt;
            Scanner scanner = new Scanner(System.in);
            opt = scanner.nextInt();
            switch (opt) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    return;
            }
        }
    }
    public static void register() throws Exception {

        Connection conn = null;
        Statement statement = null;
        System.out.println("注册开始");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/netDisk", "root", "6689203");
            statement = conn.createStatement();
            System.out.println("请输入用户名");
            Scanner sc1 = new Scanner(System.in);
            int u = sc1.nextInt();
            System.out.println("请输入密码");
            Scanner sc2 = new Scanner(System.in);
            String p = sc2.nextLine();
            String sql = "insert into user value ('" + u + "','" + p + "')";
            int num = statement.executeUpdate(sql);
            if (num > 0) {
                System.out.println("注册成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                statement = null;
            }
        }

    }
    public static void login() throws Exception {
        System.out.println("登录开始");
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            System.out.println("请输入账号");
            Scanner sc1 = new Scanner(System.in);
            int u = sc1.nextInt();
            System.out.println("请输入密码");
            Scanner sc2 = new Scanner(System.in);
            String p = sc2.nextLine();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/netDisk?autoReconnect=true&useSSL=false", "root", "6689203");
            statement = conn.createStatement();
            String sql = "select* from user";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (u == rs.getInt("username") && p.equals(rs.getString("password"))) {
                    System.out.println("登录成功");
                    System.out.println("选择下一步操作");
                    while (true) {
                        System.out.println("1.上传 2.下载 3.退出");
                        int opt;
                        Scanner scanner = new Scanner(System.in);
                        opt = scanner.nextInt();
                        switch (opt) {
                            case 1:
                                upload();
                                break;
                            case 2:
                                download();
                                break;
                            case 3:
                                return;
                        }
                    }
                } else {
                    System.out.println("登陆失败");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                statement = null;
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }

    public static void upload() throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            System.out.println("输入文件路径");
            Scanner sc1 = new Scanner(System.in);
            String path = sc1.nextLine();
            File file = new File(path);
            FileInputStream is = new FileInputStream(file);
            System.out.println("输入文件保存名称");
            Scanner sc2 = new Scanner(System.in);
            String name = sc2.nextLine();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/netDisk?autoReconnect=true&useSSL=false", "root", "6689203");
            String sql = "insert into file values(null,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "name");
            preparedStatement.setBinaryStream(2, is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                preparedStatement = null;
            }
        }
    }

    public static void download() throws Exception {
        byte[] Buffer = new byte[4096 * 5];
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        File file = new File("D:\\1.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/netDisk?autoReconnect=true&useSSL=false", "root", "6689203");
            String sql = "select idfile,filename from file";
            preparedStatement = conn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("idfile") + " " + rs.getString("filename"));
            }
            System.out.println("选择要下载的文件");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();
            String sql2 = "select file from file where idfile= '" + id;
            preparedStatement = conn.prepareStatement(sql2);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                InputStream is = rs.getBinaryStream("file");
                int size = 0;
                while ((size = is.read(Buffer)) != -1) {
                    System.out.println(size);
                    outputStream.write(Buffer, 0, size);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                preparedStatement = null;
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                rs = null;
            }

        }

    }
}