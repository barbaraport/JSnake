import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gamepanel extends JPanel implements Runnable, KeyListener { // key listener para "ouvir" os botões do teclado
	// extends faz herdar todos os atributos e métodos de uma classe
	// implements faz com que métodos da Runnable sejam reescritos na Gamepanel
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 500, HEIGHT = 500;
	
	private Thread thread; // executando coisas em paralelo
	
	private boolean running = false;
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	
	private Apple apple;
	private ArrayList<Apple> apples;
	
	private Random r;
	
	private int xCoor = 10, yCoor = 10, size = 5;
	private int ticks = 0;
	
	private boolean right = true, left = false, up = false, down = false; // controle da cobrinha

	public Gamepanel () {
	
		setFocusable(true);
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // definindo o tamanho da janela
		r = new Random();
		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();
		start();
		
	}
	
	public void start() {
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() { // para caso a cobrinha bata na parede ou coma ela mesma
		
		try {
			int again = JOptionPane.showConfirmDialog(null, "Do you want to play again?","Game over!", JOptionPane.YES_NO_OPTION);
			if (again == JOptionPane.YES_OPTION) {
				// caso a pessoa queira jogar de novo
				xCoor = 10;
				yCoor = 10;
				size = 5;
				ticks = 0;
				right = true;
				left = false;
				up = false;
				down = false;
				snake.clear();
				apples.clear();
				
				run();
				thread.join();
			}
			else {
				// caso ela não queira
				running = false;
				System.exit(0);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() { // tick é o andar da cobrinha
		
		if (snake.size() == 0) {
			b = new BodyPart(xCoor, yCoor, 10); // iniciando a cobrinha nos lugares dados
			snake.add(b);
		}
		
		if (apples.size() == 0) {
			int xCoor = r.nextInt(49); // pega um número aleatório e soma 1 nele
			int yCoor = r.nextInt(49);
			
			apple = new Apple(xCoor, yCoor, 10); // gerando uma maçã num lugar aleatório
			apples.add(apple);
		}
		
		for (int i = 0; i < apples.size(); i++) {
			
			if (xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) { // verificando se a cobrinha vai comer a maçã
				size++; // se comer a cobrinha aumenta
				apples.remove(i); // e a maçã some
				i++;
			}
		}
		for (int i = 0; i < snake.size(); i++) { // se a cobrinha bater nela mesma
			if (xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if (i != snake.size() - 1) {
					stop();
				}
			}
		}
		
		if (xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49) { // se a cobrinha chegar na parede o jogo acaba
			stop();
		}
		
		ticks++;
		
		if (ticks > 250000) {
			if (right) xCoor++; // só imaginar um eixo x. pra direita é maior, pra esquerda é menor
			if (left) xCoor--;
			if (up) yCoor--; // 
			if (down) yCoor++;
			
			ticks = 0;
			
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
			
			if (snake.size() > size) {
				snake.remove(0);
			}
		}
	}
	
	public void paint(Graphics g) { // Graphics vai renderizar a tela
		
		g.clearRect(0, 0, WIDTH, HEIGHT); // limpa o background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT); // preenche o background
		
		// os dois "fors" abaixo servem para quadricular a tela
		for (int i = 0; i < WIDTH / 10; i++) {
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		for (int i = 0; i < HEIGHT / 10; i++) {
			g.drawLine(0, i * 10, HEIGHT, i * 10);
		}
		
		for (int i = 0; i < snake.size(); i++) {
			snake.get(i).draw(g); // pegando index do tamanho da snake e desenhando ela
		}
		for (int i = 0; i < apples.size(); i++) {
			apples.get(i).draw(g);
		}
	}

	@Override
	public void run() {
		
		while (running) {
			tick();
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); // o usuário pressiona uma tecla e então pego o código dela
		
		if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !left) { // se a cobrinha não estiver indo para a esquerda e se o botão foi ->
			right = true;
			down = false;
			up = false;
		}
		if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !right) { // se a cobrinha não estiver indo para a direita e se o botão foi <-
			left = true;
			down = false;
			up = false;
		}
		if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !down) { // se a cobrinha não estiver indo para baixo e se o botão foi ^
			up = true;
			right = false;
			left = false;
		}
		if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !up) { // se a cobrinha não estiver indo para cima e se o botão foi v
			down = true;
			right = false;
			left = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
