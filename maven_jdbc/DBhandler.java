package maven_jdbc;
import java.sql.*;  

public class DBhandler {
		private String host="jdbc:mysql://localhost:3306/ecafe";
		private String user_name="root";
		private String pass="";
		private Statement statement;
		private Connection con;
		
	public void ConnectorDb(){  
		try{  
			
		Class.forName("com.mysql.jdbc.Driver");  
		
		con=DriverManager.getConnection(host,user_name,pass);   
		
		//Statement stmt=con.createStatement(); 
		
		statement=con.createStatement();
		
		}
		catch(Exception e){ 
			//System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	// Show menu function
	
	public void show_menu() {
		try {
		ResultSet rs=statement.executeQuery("select * from menu_items");
		System.out.println("******  Welcome to E-Cafe  ******");
		System.out.println("");
		System.out.println("------  Menu  ------");
		while(rs.next())  
			System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5));  
		}
		catch(Exception e){ 
			//System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	//Insert item function may ne be used but still here
	
	public void insert_item(String name, String type, int time, int price) {
	String sql = "INSERT INTO menu_items(item_name, item_type, item_time, item_price) VALUES (?,?,?,?)";
		
		try {
			
		PreparedStatement stmt=con.prepareStatement(sql);
		stmt.setString(1,name);
		stmt.setString(2,type);
		stmt.setInt(3,time);		//time in minutes
		stmt.setInt(4, price);
		int i=stmt.executeUpdate();
		//System.out.println("\n\n"+i+" record added");
		
	} 
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
	}
	
	
	//placing order in orders table
	public int place_order(String name, String addr, String type, int bill,int time) {
		String sql = "INSERT INTO orders(customer_name, customer_address, date_time, bill, delivery_type, estimate_time) VALUES (?,?,?,?,?,?)";
		
		try {
			java.util.Date myDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
		PreparedStatement stmt=con.prepareStatement(sql);
		stmt.setString(1,name);
		stmt.setString(2,addr);
		stmt.setDate(3,sqlDate);
		stmt.setInt(4, bill);
		stmt.setString(5,type);
		stmt.setInt(6, time);
		int i=stmt.executeUpdate();
		//System.out.println("\n\n"+i+" record added");
		
	} 
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		ResultSet rs;
		int lastid=0;
		try {
			rs = statement.executeQuery("select last_insert_id() as last_id from orders");
			while(rs.next()) {
			lastid = rs.getInt(1);
			}
			System.out.println("Order ID is: " +lastid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lastid;
		
	}
	
	
	//inserting into order_items table
	public void insert_oder_items(int order_id, int item_id, int qty) {
		String sql = "INSERT INTO order_items(order_id, item_id, qty, status) VALUES (?,?,?,?)";
		try {
			
			PreparedStatement stmt=con.prepareStatement(sql);
			stmt.setInt(1,order_id);
			stmt.setInt(2,item_id);
			stmt.setInt(3,qty);
			stmt.setString(4, "Inprogress");
			int i=stmt.executeUpdate();
			//System.out.println("\n\n"+i+" record added");
			
		} 
			catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	//calculating time for 
	public int[] calculate_time_total(int[] arr) {
		
		CallableStatement callableStatement =null;
		
		try {
			callableStatement = con.prepareCall("{call calculate_time(?)}");
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int[] totals=new int[2]; //totals[0]=> time | totals[1]=> bill
		totals[0]=0;
		totals[1]=0;
		
		for(int i=0;i<arr.length;i++) {
		
			try {
				callableStatement.setInt(1,arr[i]);
				//ResultSet rs=statement.executeQuery("select item_time, item_price from menu_items where id="+arr[i]);
				ResultSet rs=callableStatement.executeQuery();
				
				while(rs.next())
				{
					totals[0]+=rs.getInt(1);
					totals[1]+=rs.getInt(2);
					//System.out.println(rs.getInt(1));}
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(Exception e)	{
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }
		}
		return totals;
	}
}