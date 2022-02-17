package cronometro;

import java.awt.BorderLayout;
import java.awt.Dimension; // bib para dimensoes
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog; //bib de janelas
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tela extends JDialog {
	
	private JPanel janela = new JPanel(new GridBagLayout());
	
	private JLabel data = new JLabel();
	private JLabel hora = new JLabel();
	
	private JLabel label2 = new JLabel("Cronometro");
	private JTextField campo2 = new JTextField("00:00:00");
	

	private JButton iniciar = new JButton("Começar");
	private JButton parar = new JButton("Parar");
	private JButton zerar = new JButton("Zerar");
	
	private Thread threadDataHora;
	private Thread threadCronometro;
	
	private Runnable dataHora = new Runnable() {
		@Override
		public void run() {
			while(true) {
				data.setText("Data: " + (new SimpleDateFormat("dd/MM/yyyy")
						.format(Calendar.getInstance().getTime())));
				hora.setText("Hora: " + (new SimpleDateFormat("HH:mm")
						.format(Calendar.getInstance().getTime())));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private int count = 0;
	
	private Runnable cronometro = new Runnable() {
		@Override
		public void run() {
			while(true) {
				count++;
				int seg = count % 60;
				int min = count / 60;
				int hora = min / 60;
				min %= 60;
				campo2.setText(String.format("%02d:%02d:%02d", hora, min, seg));
				
				try {
					Thread.sleep(1000); //sleep de 1 segundo
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	
	Tela(){ //construtor da tela
		
		setTitle("Cronometro"); // titulo da tela
		setSize(new Dimension(380, 260)); //diensiona a tela
		setLocationRelativeTo(null); //centraliza a janela pois nao existe outra referencia (null)
		setResizable(false);  //nao deixa redimensionar a janela
		setAlwaysOnTop(true);  //sempre na frente de todas as janelas
		
		GridBagConstraints grade = new GridBagConstraints(); //controla posicionmento nas janelas
		grade.gridx = 0;
		grade.gridy = 0;
		grade.anchor = grade.CENTER;
		grade.insets = new Insets(5, 10, 5, 10);
		
		grade.gridwidth = 2;
		
		data.setPreferredSize(new Dimension(220, 30)); //cria a Label
		janela.add(data, grade); //adiciona a label a janela
		
		hora.setPreferredSize(new Dimension(220, 30));
		grade.gridy++;
		janela.add(hora, grade);
		
		label2.setPreferredSize(new Dimension(220, 30));
		grade.gridy++;
		janela.add(label2, grade);
		
		campo2.setPreferredSize(new Dimension(220,30));
		grade.gridy++;
		campo2.setEditable(false); //nao é possivel editar o campo
		janela.add(campo2, grade);
		
		grade.gridwidth = 1;
		
		iniciar.setPreferredSize(new Dimension(100, 30));
		grade.gridy++;
		janela.add(iniciar, grade);
		
		parar.setPreferredSize(new Dimension(100, 30));
		grade.gridx++;
		janela.add(parar, grade);
		
		zerar.setPreferredSize(new Dimension(100, 30));
		grade.gridx++;
		janela.add(zerar, grade);
		
		
		iniciar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				threadCronometro = new Thread(cronometro);
				threadCronometro.start();
				
				iniciar.setEnabled(false);	
				parar.setEnabled(true);	
				zerar.setEnabled(false);
				
			}	
		});
		
		parar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				threadCronometro.stop();
				
				iniciar.setEnabled(true);	
				iniciar.setText("Continuar");
				parar.setEnabled(false);
				zerar.setEnabled(true);
			}
		});
		
		zerar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				threadCronometro.stop();
				count = 0;
				
				iniciar.setText("Iniciar");
				iniciar.setEnabled(true);	
				parar.setEnabled(false);
				zerar.setEnabled(false);
				
				campo2.setText("00:00:00");
			}
		});
		
		threadDataHora = new Thread(dataHora);
		threadDataHora.start();
		
		parar.setEnabled(false);
		zerar.setEnabled(false);
		add(janela, BorderLayout.WEST); //adiciona a janela a janela principal
		setVisible(true); // torna a tela visivel ,sempre sera o ultimo a ser execultado
	}

}
