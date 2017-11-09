package maven_jdbc;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
    	
    	
    	Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date current=null,eleven=null,ten=null;
		try {
			current = sdf.parse( sdf.format(cal.getTime()));
			eleven = sdf.parse("11:00");
	        ten=sdf.parse("22:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBhandler db = new DBhandler();
    	db.ConnectorDb();
       //While loop for shop options like this: 
       while( current.after(eleven)&& current.before(ten)){
    	
    	//taking details of the buyer.
    	Scanner scanner=new Scanner(System.in);
    	System.out.println("Enter name");
    	String name=scanner.nextLine();
    	System.out.println("Enter address");
    	String address=scanner.nextLine();
    	System.out.println("Delivery Type\n1 for take_away\n2 for Delivery.");
    	int check=scanner.nextInt();
    	String type;
    	if(check==1)type="Take_away"; else type="Delivery";
    	
    	//Showing Menu.
    	db.show_menu();
    	
    	int i=1,a=0;
    	int[] selected_Menu=new int[10];
    	
    	while(true) {
    		System.out.println("Enter -1 for Exit from Menu. OR\nSelect Menu Item Number:");
    		i=scanner.nextInt();
    		if(i==-1)
    			break;
    		else 
    			selected_Menu[a]=i;
    		a++;
    	}
    	
    	if(selected_Menu.length!=0) {
    	int[] totals=db.calculate_time_total(selected_Menu);
    	System.out.println("Estimated Time: "+totals[0]);
    	System.out.println("Estimated total: "+totals[1]);
		    	if(check==1) {
		    		totals[0]+=5;
		    	}else {
		    		totals[0]+=15;
		    	}
		    	
		int o_id=db.place_order(name, address, type,totals[1],totals[0]);
		
		Arrays.sort(selected_Menu);
        int len=selected_Menu[selected_Menu.length-1]+1;
        int count[]=new int[len];
        for(int n:selected_Menu){
            count[n]++;
        }
        for(int j=1;j<count.length;j++){
            if(count[j]>=1){
            //System.out.println("count:"+j+"---"+count[j]);
            //TODO: insert order and item_id quantity
            db.insert_oder_items(o_id, j, count[j]);	
            }
        }
		
    	}else{
    		//TODO: break the loop
    	}
       }
       System.out.println("Shop is closed!!!");
    	
    	//menu m1 = new menu();
        //m1.display_menu();
        //order o1 = new order();
        //o1.placeOrder();
    }
    
    
}

