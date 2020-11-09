import javax.swing.JFrame;

public class Main {
	
	public Main () {
		
		JFrame frame = new JFrame();
		Gamepanel gamepanel = new Gamepanel();
		
		frame.add(gamepanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false); // n�o deixa o JFrame redimensionar
		frame.setTitle("JSnake");
		frame.pack(); // torna redimension�vel o JFrame do jogo
		frame.setLocationRelativeTo(null); // posicionar de acordo com alguma outra janela. como est� null fica no centro
		frame.setVisible(true);
		
	}

	public static void main(String[] args) {

		new Main();
		
	}

}
