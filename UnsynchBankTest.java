package com.Thread.TongBu;

public class UnsynchBankTest {

	public static final int NACCOUNTS = 10;//10个客户
	public static final int INITTAL_BALANCE = 10000;//每个客户存款10000

	public static void main(String[] arggs){
		Bank b = new Bank(NACCOUNTS,INITTAL_BALANCE);
		for(int i=0;i<NACCOUNTS;i++){
			TransferThread t = new TransferThread(b,i,INITTAL_BALANCE);
			t.setPriority(Thread.NORM_PRIORITY+i%2);
			t.start();
		}
	}
}

class Bank
{
	public static final int NTEST = 10000;
	private final int[] accounts;
	private long ntransacts = 0;
	public Bank(int n,int initialBalance){
		accounts = new int[n];
		for(int i=0;i<accounts.length;i++){
			accounts[i] = initialBalance;
		}
		ntransacts = 0;
	}
	public void test(){
		int sum = 0;
		for(int i=0;i<accounts.length;i++){
			sum +=accounts[i];
		}
		System.out.println("transaction:"+ntransacts+"sum:"+sum);
	}
	public int size(){
		return accounts.length;
	}
	public void transfer(int fromAccount, int toAccount, int amount) throws InterruptedException{
		accounts[fromAccount] -= amount;
		accounts[toAccount] += amount;
		ntransacts++;
		if(ntransacts % NTEST == 0)test();
		
	}
}

class TransferThread extends Thread
{
	private Bank bank;
	private int fromAccount;
	private int maxAmount;
	private static final int REPS = 1000;
	public TransferThread(Bank b,int from,int max){
		bank = b;
		fromAccount = from;
		maxAmount = max;
	}
	public void run(){
		try {
			while(!interrupted()){
				for(int i=0;i<REPS;i++){
					int toAccount = (int)(bank.size()*Math.random());
					int amount = (int)(maxAmount*Math.random()/REPS);
					bank.transfer(fromAccount,toAccount,amount);
					sleep(1);
				}
			}
		} catch (InterruptedException e) {
		
		}
	}
}
