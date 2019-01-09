import org.big.entity.Taxon;

public class TestEntityValue {

	public static void main(String[] args) {
		Taxon t = new Taxon();
		System.out.println(t.getRemark());
		handleTaxon(t);
		System.out.println(t.getRemark());
		System.out.println("02 getChname:\t"+t.getChname());
		t.setChname("中文名");
		System.out.println("03 getChname:\t"+t.getChname());
		Runnable runnable = new Runnable() {  
            public void run() {  
                while (true) {  
                   
                    try {  
                        Thread.sleep(10000);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    // ------- code for task to run  
                	t.setChname("不是中文名");
                    // ------- ends here  
                }  
            }  
        };  
        Thread thread = new Thread(runnable);  
        thread.start();  

	}

	private static void handleTaxon(Taxon t) {
		t.setRemark("12345");
		System.out.println("01 getChname:\t"+t.getChname());
		 Runnable runnable = new Runnable() {  
	            public void run() {  
	                while (true) {  
	                    // ------- code for task to run  
	                	System.out.println("code for task to run   getChname:\t"+t.getChname());
	                    // ------- ends here  
	                    try {  
	                        Thread.sleep(1000);  
	                    } catch (InterruptedException e) {  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	        };  
	        Thread thread = new Thread(runnable);  
	        thread.start();  
		
	}

}
