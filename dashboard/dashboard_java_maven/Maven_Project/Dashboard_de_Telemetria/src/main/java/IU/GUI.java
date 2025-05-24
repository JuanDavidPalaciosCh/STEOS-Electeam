package IU;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Datos.Pista;
import Datos.Telemetria;
import Datos.Vehiculo;
import POO.Dashboard_de_Telemetria.App;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel velocidadLabel;
	private JLabel temperaturaLabel;
	private JLabel corrienteLabel;
	private JLabel voltajeLabel;
	private JLabel PotenciaLabel;
	private JPanel Graph;
	private static Telemetria telemetria;
	private XYSeries velocidadSeries;
	private XYSeries temperaturaSeries;
	private XYSeries corrienteSeries;
	private XYSeries voltajeSeries;
	private XYSeries potenciaSeries;
	private ChartPanel chartPanel;
	private long startTime;
	private JPanel IndicadorHM_1;
	private JPanel IndicadorHM;
	private JLabel ValDist;
	private JLabel VelProm;
	private JLabel PotProm;
	private JLabel TiempoMin;
	private JLabel VariableSel;
	private String Optselected;
	private JTextField NombrePista;
	private JTextField DistanciaPista;
	private JTextField Ubicacion;
	private static Vehiculo Sharky;
	private static Vehiculo McUN;
	private JLabel Nombrelbl;
	private JLabel NumRuedaslbl;
	private JLabel pesolbl;
	private JLabel Categorialbl;
	private JLabel Pilotolbl;
	private JLabel CarImg;
	private JLabel BatImg;
	



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI(telemetria,Sharky,McUN);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI(Telemetria telemetria, Vehiculo sharky, Vehiculo mcun) {
		Optselected = "Velocidad";
	    GUI.Sharky = sharky;
	    GUI.McUN = mcun;
		startTime = System.currentTimeMillis();
		GUI.telemetria = telemetria;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1080, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		velocidadSeries = new XYSeries("Velocidad");
	    temperaturaSeries = new XYSeries("Temperatura");
	    corrienteSeries = new XYSeries("Corriente");
	    voltajeSeries = new XYSeries("Voltaje");
	    potenciaSeries = new XYSeries("Potencia");

        String path = System.getProperty("user.dir");
		
		JPanel Menu = new JPanel();
		Menu.setBackground(new Color(192, 192, 192));
		Menu.setBounds(0, 0, 283, 703);
		contentPane.add(Menu);
		Menu.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/LogoImg.png"));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 283, 70);
		Menu.add(lblNewLabel);
		
		JComboBox VehicleSelect = new JComboBox();
		VehicleSelect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		VehicleSelect.setEditable(true);
		VehicleSelect.setModel(new DefaultComboBoxModel(new String[] {"Seleccione un Vehículo", "Sharky", "McUN"}));
		VehicleSelect.setToolTipText("");
		VehicleSelect.setBounds(10, 80, 263, 21);
		Menu.add(VehicleSelect);
		
		VehicleSelect.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String selectedVehicle = (String) VehicleSelect.getSelectedItem();
	            if ("Sharky".equals(selectedVehicle)) {
	                actualizarDatosVehiculo(Sharky);
	            } else if ("McUN".equals(selectedVehicle)) {
	                actualizarDatosVehiculo(McUN);
	            } else {
	                limpiarDatosVehiculo();
	            }
	        }
	    });
		
		
		
		JPanel NombreVehiculo = new JPanel();
		NombreVehiculo.setBounds(10, 125, 263, 21);
		Menu.add(NombreVehiculo);
		NombreVehiculo.setLayout(null);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(98, 0, 165, 21);
		NombreVehiculo.add(panel_5);
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		Nombrelbl = new JLabel("");
		Nombrelbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		Nombrelbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(Nombrelbl);
		
		JPanel panel_5_1 = new JPanel();
		panel_5_1.setBounds(0, 0, 100, 21);
		NombreVehiculo.add(panel_5_1);
		panel_5_1.setBackground(new Color(164, 220, 141));
		panel_5_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Nombre");
		lblNewLabel_1.setBackground(new Color(164, 220, 141));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_1.add(lblNewLabel_1);
		
		JPanel NumRuedas = new JPanel();
		NumRuedas.setBounds(10, 156, 263, 21);
		Menu.add(NumRuedas);
		NumRuedas.setLayout(null);
		
		JPanel panel_5_2 = new JPanel();
		panel_5_2.setBounds(98, 0, 165, 21);
		NumRuedas.add(panel_5_2);
		panel_5_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		NumRuedaslbl = new JLabel("");
		NumRuedaslbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		NumRuedaslbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_2.add(NumRuedaslbl);
		
		JPanel panel_5_1_1 = new JPanel();
		panel_5_1_1.setBounds(0, 0, 100, 21);
		NumRuedas.add(panel_5_1_1);
		panel_5_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_1 = new JLabel("N° de Ruedas");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_1.add(lblNewLabel_1_1);
		
		JPanel Peso = new JPanel();
		Peso.setBounds(10, 187, 263, 21);
		Menu.add(Peso);
		Peso.setLayout(null);
		
		JPanel panel_5_3 = new JPanel();
		panel_5_3.setBounds(98, 0, 165, 21);
		Peso.add(panel_5_3);
		panel_5_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		pesolbl = new JLabel("");
		pesolbl.setHorizontalAlignment(SwingConstants.CENTER);
		pesolbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_5_3.add(pesolbl);
		
		JPanel panel_5_1_2 = new JPanel();
		panel_5_1_2.setBounds(0, 0, 100, 21);
		Peso.add(panel_5_1_2);
		panel_5_1_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_2.setBackground(new Color(164, 220, 141));
		panel_5_1_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_2 = new JLabel("Peso (kg)");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_2.setBackground(new Color(164, 220, 141));
		panel_5_1_2.add(lblNewLabel_1_2);
		
		JPanel Categoria = new JPanel();
		Categoria.setBounds(10, 218, 263, 21);
		Menu.add(Categoria);
		Categoria.setLayout(null);
		
		JPanel panel_5_4 = new JPanel();
		panel_5_4.setBounds(98, 0, 165, 21);
		Categoria.add(panel_5_4);
		panel_5_4.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		Categorialbl = new JLabel("");
		Categorialbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		Categorialbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_4.add(Categorialbl);
		
		JPanel panel_5_1_3 = new JPanel();
		panel_5_1_3.setBounds(0, 0, 100, 21);
		Categoria.add(panel_5_1_3);
		panel_5_1_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3.setBackground(new Color(164, 220, 141));
		panel_5_1_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3 = new JLabel("Categoría");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3.setBackground(new Color(164, 220, 141));
		panel_5_1_3.add(lblNewLabel_1_3);
		
		JPanel Imagen = new JPanel();
		Imagen.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Imagen.setBackground(new Color(192, 192, 192));
		Imagen.setBounds(10, 292, 263, 200);
		Menu.add(Imagen);
		Imagen.setLayout(new GridLayout(1, 0, 0, 0));
		
		CarImg = new JLabel("");
		CarImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Sharky.png"));
		CarImg.setHorizontalAlignment(SwingConstants.CENTER);
		Imagen.add(CarImg);
		
		JPanel Piloto = new JPanel();
		Piloto.setLayout(null);
		Piloto.setBounds(10, 249, 263, 21);
		Menu.add(Piloto);
		
		JPanel panel_5_4_2 = new JPanel();
		panel_5_4_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_4_2.setBounds(98, 0, 165, 21);
		Piloto.add(panel_5_4_2);
		panel_5_4_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		Pilotolbl = new JLabel("");
		Pilotolbl.setHorizontalAlignment(SwingConstants.CENTER);
		Pilotolbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_5_4_2.add(Pilotolbl);
		
		JPanel panel_5_1_3_2 = new JPanel();
		panel_5_1_3_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3_2.setBackground(new Color(164, 220, 141));
		panel_5_1_3_2.setBounds(0, 0, 100, 21);
		Piloto.add(panel_5_1_3_2);
		panel_5_1_3_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3_2 = new JLabel("Piloto");
		lblNewLabel_1_3_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3_2.setBackground(new Color(164, 220, 141));
		panel_5_1_3_2.add(lblNewLabel_1_3_2);
		
		JPanel panel_5_5 = new JPanel();
		panel_5_5.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_5.setBounds(36, 502, 206, 21);
		Menu.add(panel_5_5);
		panel_5_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblIngreseLosDetalles = new JLabel("Ingrese los detalles de la pista");
		lblIngreseLosDetalles.setHorizontalAlignment(SwingConstants.CENTER);
		lblIngreseLosDetalles.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_5_5.add(lblIngreseLosDetalles);
		
		JPanel NombreVehiculo_1 = new JPanel();
		NombreVehiculo_1.setLayout(null);
		NombreVehiculo_1.setBounds(10, 546, 263, 21);
		Menu.add(NombreVehiculo_1);
		
		JPanel panel_5_6 = new JPanel();
		panel_5_6.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_6.setBounds(98, 0, 165, 21);
		NombreVehiculo_1.add(panel_5_6);
		panel_5_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		NombrePista = new JTextField();
		NombrePista.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_6.add(NombrePista);
		NombrePista.setColumns(10);
		
		JPanel panel_5_1_4 = new JPanel();
		panel_5_1_4.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_4.setBackground(new Color(138, 191, 217));
		panel_5_1_4.setBounds(0, 0, 100, 21);
		NombreVehiculo_1.add(panel_5_1_4);
		panel_5_1_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_4 = new JLabel("Nombre");
		lblNewLabel_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_4.setBackground(new Color(138, 191, 217));
		panel_5_1_4.add(lblNewLabel_1_4);
		
		JPanel NumRuedas_1 = new JPanel();
		NumRuedas_1.setLayout(null);
		NumRuedas_1.setBounds(10, 577, 263, 21);
		Menu.add(NumRuedas_1);
		
		JPanel panel_5_2_1 = new JPanel();
		panel_5_2_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_2_1.setBounds(98, 0, 165, 21);
		NumRuedas_1.add(panel_5_2_1);
		panel_5_2_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		DistanciaPista = new JTextField();
		DistanciaPista.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_2_1.add(DistanciaPista);
		DistanciaPista.setColumns(10);
		
		JPanel panel_5_1_1_1 = new JPanel();
		panel_5_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_1_1.setBackground(new Color(138, 191, 217));
		panel_5_1_1_1.setBounds(0, 0, 100, 21);
		NumRuedas_1.add(panel_5_1_1_1);
		panel_5_1_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Distancia (km)");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_1_1.add(lblNewLabel_1_1_1);
		
		JPanel Peso_1 = new JPanel();
		Peso_1.setLayout(null);
		Peso_1.setBounds(10, 608, 263, 21);
		Menu.add(Peso_1);
		
		JPanel panel_5_3_1 = new JPanel();
		panel_5_3_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_3_1.setBounds(98, 0, 165, 21);
		Peso_1.add(panel_5_3_1);
		panel_5_3_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		Ubicacion = new JTextField();
		Ubicacion.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_3_1.add(Ubicacion);
		Ubicacion.setColumns(10);
		
		JPanel panel_5_1_2_1 = new JPanel();
		panel_5_1_2_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_2_1.setBackground(new Color(138, 191, 217));
		panel_5_1_2_1.setBounds(0, 0, 100, 21);
		Peso_1.add(panel_5_1_2_1);
		panel_5_1_2_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Ubicación");
		lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_2_1.setBackground(new Color(164, 220, 141));
		panel_5_1_2_1.add(lblNewLabel_1_2_1);
		
		JButton Confirmar = new JButton("Confirmar");
		Confirmar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				App.definirPista(NombrePista.getText(),Ubicacion.getText(),Double.parseDouble(DistanciaPista.getText()));
				NombrePista.setText(Pista.getNombre());
				DistanciaPista.setText(Double.toString(Pista.getDistancia()));
				Ubicacion.setText(Pista.getUbicacion());
			}
		});
		Confirmar.setBackground(new Color(0, 255, 0));
		Confirmar.setBounds(159, 649, 114, 21);
		Menu.add(Confirmar);
		
		JPanel Variable_Options = new JPanel();
		Variable_Options.setBackground(new Color(255, 255, 255));
		Variable_Options.setBounds(863, 0, 203, 703);
		contentPane.add(Variable_Options);
		Variable_Options.setLayout(null);
		
		JPanel Velocidad = new JPanel();
		Velocidad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Velocidad");
			}
		});
		Velocidad.setBounds(10, 36, 183, 97);
		Variable_Options.add(Velocidad);
		Velocidad.setLayout(null);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(0,0,0));
		panel_6.setForeground(new Color(0, 0, 0));
		panel_6.setBounds(0, 0, 183, 33);
		Velocidad.add(panel_6);
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		JLabel lblNewLabel_2 = new JLabel("Velocidad");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblNewLabel_2);
		lblNewLabel_2.setForeground(new Color(255,255,255));
		
		JPanel panel_6_1 = new JPanel();
		panel_6_1.setBackground(new Color(160,222,137));
		panel_6_1.setBounds(0, 32, 183, 65);
		Velocidad.add(panel_6_1);
		panel_6_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_1.setLayout(null);

		
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setBounds(16, 10, 50, 44);
		lblNewLabel_4.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/GaugeVel.png"));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6_1.add(lblNewLabel_4);
		
		velocidadLabel = new JLabel("");
		velocidadLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		velocidadLabel.setBounds(76, 0, 97, 63);
		panel_6_1.add(velocidadLabel);
		
		JPanel Temperatura = new JPanel();
		Temperatura.setLayout(null);
		Temperatura.setBounds(10, 169, 183, 97);
		Variable_Options.add(Temperatura);
		
		JPanel panel_6_2 = new JPanel();
		panel_6_2.setForeground(Color.GRAY);
		panel_6_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_2.setBackground(Color.GRAY);
		panel_6_2.setBounds(0, 0, 183, 33);
		Temperatura.add(panel_6_2);
		panel_6_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_2_1 = new JLabel("Temperatura");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_6_2.add(lblNewLabel_2_1);
		lblNewLabel_2_1.setForeground(new Color(0,0,0));
		
		JPanel panel_6_1_1 = new JPanel();
		panel_6_1_1.setBackground(new Color(192, 192, 192));
		panel_6_1_1.setLayout(null);
		panel_6_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_1_1.setBounds(0, 32, 183, 65);
		Temperatura.add(panel_6_1_1);
		
		JLabel lblNewLabel_4_1 = new JLabel("");
		lblNewLabel_4_1.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Temp.png"));
		lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_1.setBounds(16, 10, 50, 44);
		panel_6_1_1.add(lblNewLabel_4_1);
		
		temperaturaLabel = new JLabel("");
		temperaturaLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		temperaturaLabel.setBounds(76, 0, 97, 63);
		panel_6_1_1.add(temperaturaLabel);
		
		JPanel Corriente = new JPanel();
		Corriente.setLayout(null);
		Corriente.setBounds(10, 302, 183, 97);
		Variable_Options.add(Corriente);
		
		JPanel panel_6_3 = new JPanel();
		panel_6_3.setForeground(Color.GRAY);
		panel_6_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_3.setBackground(Color.GRAY);
		panel_6_3.setBounds(0, 0, 183, 33);
		Corriente.add(panel_6_3);
		panel_6_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_2_2 = new JLabel("Corriente");
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_6_3.add(lblNewLabel_2_2);
		lblNewLabel_2_2.setForeground(new Color(0,0,0));
		
		JPanel panel_6_1_2 = new JPanel();
		panel_6_1_2.setBackground(new Color(192, 192, 192));
		panel_6_1_2.setLayout(null);
		panel_6_1_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_1_2.setBounds(0, 32, 183, 65);
		Corriente.add(panel_6_1_2);
		
		JLabel lblNewLabel_4_2 = new JLabel("");
		lblNewLabel_4_2.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Corr.png"));
		lblNewLabel_4_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_2.setBounds(16, 10, 50, 44);
		panel_6_1_2.add(lblNewLabel_4_2);
		
		corrienteLabel = new JLabel("");
		corrienteLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		corrienteLabel.setBounds(76, 0, 97, 63);
		panel_6_1_2.add(corrienteLabel);
		
		JPanel Voltaje = new JPanel();
		Voltaje.setLayout(null);
		Voltaje.setBounds(10, 435, 183, 97);
		Variable_Options.add(Voltaje);
		
		JPanel panel_6_4 = new JPanel();
		panel_6_4.setForeground(Color.GRAY);
		panel_6_4.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_4.setBackground(Color.GRAY);
		panel_6_4.setBounds(0, 0, 183, 33);
		Voltaje.add(panel_6_4);
		panel_6_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_2_3 = new JLabel("Voltaje");
		lblNewLabel_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_6_4.add(lblNewLabel_2_3);
		lblNewLabel_2_3.setForeground(new Color(0,0,0));
		
		JPanel panel_6_1_3 = new JPanel();
		panel_6_1_3.setBackground(new Color(192, 192, 192));
		panel_6_1_3.setLayout(null);
		panel_6_1_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_1_3.setBounds(0, 32, 183, 65);
		Voltaje.add(panel_6_1_3);
		
		JLabel lblNewLabel_4_3 = new JLabel("");
		lblNewLabel_4_3.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Volt.png"));
		lblNewLabel_4_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_3.setBounds(16, 10, 50, 44);
		panel_6_1_3.add(lblNewLabel_4_3);
		
		voltajeLabel = new JLabel("");
		voltajeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		voltajeLabel.setBounds(76, 0, 97, 63);
		panel_6_1_3.add(voltajeLabel);
		
		JPanel Potencia = new JPanel();
		Potencia.setLayout(null);
		Potencia.setBounds(10, 568, 183, 97);
		Variable_Options.add(Potencia);
		
		JPanel panel_6_5 = new JPanel();
		panel_6_5.setForeground(Color.GRAY);
		panel_6_5.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_5.setBackground(Color.GRAY);
		panel_6_5.setBounds(0, 0, 183, 33);
		Potencia.add(panel_6_5);
		panel_6_5.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_2_4 = new JLabel("Potencia");
		lblNewLabel_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_6_5.add(lblNewLabel_2_4);
		lblNewLabel_2_4.setForeground(new Color(0,0,0));
		
		JPanel panel_6_1_4 = new JPanel();
		panel_6_1_4.setBackground(new Color(192, 192, 192));
		panel_6_1_4.setLayout(null);
		panel_6_1_4.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_6_1_4.setBounds(0, 32, 183, 65);
		Potencia.add(panel_6_1_4);
		
		JLabel lblNewLabel_4_4 = new JLabel("");
		lblNewLabel_4_4.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Pot.png"));
		lblNewLabel_4_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_4.setBounds(16, 10, 50, 44);
		panel_6_1_4.add(lblNewLabel_4_4);
		
		PotenciaLabel = new JLabel("");
		PotenciaLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		PotenciaLabel.setBounds(76, 0, 97, 63);
		panel_6_1_4.add(PotenciaLabel);
		
		JPanel Details = new JPanel();
		Details.setBackground(new Color(255, 255, 255));
		Details.setBounds(282, 472, 582, 231);
		contentPane.add(Details);
		Details.setLayout(null);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(192, 192, 192));
		panel_8.setBounds(22, 5, 538, 216);
		Details.add(panel_8);
		panel_8.setLayout(null);
		
		JPanel SelectedVar = new JPanel();
		SelectedVar.setBackground(new Color(192, 192, 192));
		SelectedVar.setBounds(113, 5, 321, 62);
		panel_8.add(SelectedVar);
		SelectedVar.setLayout(new GridLayout(1, 0, 0, 0));
		
		VariableSel = new JLabel("");
		VariableSel.setFont(new Font("Tahoma", Font.PLAIN, 46));
		VariableSel.setHorizontalAlignment(SwingConstants.CENTER);
		SelectedVar.add(VariableSel);
		
		JPanel TiempoTrans = new JPanel();
		TiempoTrans.setLayout(null);
		TiempoTrans.setBounds(10, 77, 263, 21);
		panel_8.add(TiempoTrans);
		
		JPanel panel_5_4_1 = new JPanel();
		panel_5_4_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_4_1.setBounds(182, 0, 81, 21);
		TiempoTrans.add(panel_5_4_1);
		panel_5_4_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		TiempoMin = new JLabel("");
		TiempoMin.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_4_1.add(TiempoMin);
		
		JPanel panel_5_1_3_1 = new JPanel();
		panel_5_1_3_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3_1.setBackground(new Color(218, 132, 255));
		panel_5_1_3_1.setBounds(0, 0, 184, 21);
		TiempoTrans.add(panel_5_1_3_1);
		panel_5_1_3_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3_1 = new JLabel("Tiempo Transcurrido (min)");
		lblNewLabel_1_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3_1.setBackground(new Color(164, 220, 141));
		panel_5_1_3_1.add(lblNewLabel_1_3_1);
		
		JPanel DistRecorrida = new JPanel();
		DistRecorrida.setLayout(null);
		DistRecorrida.setBounds(10, 108, 263, 21);
		panel_8.add(DistRecorrida);
		
		JPanel Dist = new JPanel();
		Dist.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		Dist.setBounds(181, 0, 82, 21);
		DistRecorrida.add(Dist);
		Dist.setLayout(new GridLayout(0, 1, 0, 0));
		
		ValDist = new JLabel("");
		ValDist.setHorizontalAlignment(SwingConstants.CENTER);
		Dist.add(ValDist);
		
		JPanel panel_5_1_3_1_1 = new JPanel();
		panel_5_1_3_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3_1_1.setBackground(new Color(218, 132, 255));
		panel_5_1_3_1_1.setBounds(0, 0, 182, 21);
		DistRecorrida.add(panel_5_1_3_1_1);
		panel_5_1_3_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3_1_1 = new JLabel("Distancia Recorrida (km)");
		lblNewLabel_1_3_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_3_1_1.add(lblNewLabel_1_3_1_1);
		
		JPanel VelocidadProm = new JPanel();
		VelocidadProm.setLayout(null);
		VelocidadProm.setBounds(10, 139, 263, 21);
		panel_8.add(VelocidadProm);
		
		JPanel panel_5_4_1_1_1 = new JPanel();
		panel_5_4_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_4_1_1_1.setBounds(185, 0, 78, 21);
		VelocidadProm.add(panel_5_4_1_1_1);
		panel_5_4_1_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		VelProm = new JLabel("");
		VelProm.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_4_1_1_1.add(VelProm);
		
		JPanel panel_5_1_3_1_1_1 = new JPanel();
		panel_5_1_3_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3_1_1_1.setBackground(new Color(218, 132, 255));
		panel_5_1_3_1_1_1.setBounds(0, 0, 190, 21);
		VelocidadProm.add(panel_5_1_3_1_1_1);
		panel_5_1_3_1_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3_1_1_1 = new JLabel("Velocidad Promedio (km/h)");
		lblNewLabel_1_3_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3_1_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_3_1_1_1.add(lblNewLabel_1_3_1_1_1);
		
		JPanel PotenciaPromedio = new JPanel();
		PotenciaPromedio.setLayout(null);
		PotenciaPromedio.setBounds(10, 170, 263, 21);
		panel_8.add(PotenciaPromedio);
		
		JPanel panel_5_4_1_1_1_1 = new JPanel();
		panel_5_4_1_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_4_1_1_1_1.setBounds(187, 0, 76, 21);
		PotenciaPromedio.add(panel_5_4_1_1_1_1);
		panel_5_4_1_1_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		PotProm = new JLabel("");
		PotProm.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5_4_1_1_1_1.add(PotProm);
		
		JPanel panel_5_1_3_1_1_1_1 = new JPanel();
		panel_5_1_3_1_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_5_1_3_1_1_1_1.setBackground(new Color(218, 132, 255));
		panel_5_1_3_1_1_1_1.setBounds(0, 0, 190, 21);
		PotenciaPromedio.add(panel_5_1_3_1_1_1_1);
		panel_5_1_3_1_1_1_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1_3_1_1_1_1 = new JLabel("Potencia Promedio (W)");
		lblNewLabel_1_3_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3_1_1_1_1.setBackground(new Color(164, 220, 141));
		panel_5_1_3_1_1_1_1.add(lblNewLabel_1_3_1_1_1_1);
		
		JLabel HombreMuertoInd = new JLabel("Hombre Muerto");
		HombreMuertoInd.setFont(new Font("Tahoma", Font.BOLD, 15));
		HombreMuertoInd.setHorizontalAlignment(SwingConstants.CENTER);
		HombreMuertoInd.setBounds(357, 77, 136, 13);
		panel_8.add(HombreMuertoInd);
		
		JLabel TemperaturaInd = new JLabel("Temperatura");
		TemperaturaInd.setHorizontalAlignment(SwingConstants.CENTER);
		TemperaturaInd.setFont(new Font("Tahoma", Font.BOLD, 15));
		TemperaturaInd.setBounds(357, 147, 136, 13);
		panel_8.add(TemperaturaInd);
		
		IndicadorHM = new JPanel();
		IndicadorHM.setBackground(new Color(0, 255, 64));
		IndicadorHM.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		if(telemetria.getHombreMuerto()) {
			IndicadorHM.setBackground(Color.GREEN);
		}else {
			IndicadorHM.setBackground(Color.RED);
		}
		IndicadorHM.setBounds(403, 100, 54, 21);
		panel_8.add(IndicadorHM);
		IndicadorHM.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("OK");
		lblNewLabel_3.setBounds(0, 0, 54, 21);
		IndicadorHM.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBackground(new Color(0, 255, 0));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		
		IndicadorHM_1 = new JPanel();
		IndicadorHM_1.setLayout(null);
		IndicadorHM_1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		IndicadorHM_1.setBackground(new Color(0, 255, 0));
		if(telemetria.getTemperatura()<35.0) {
			IndicadorHM_1.setBackground(Color.GREEN);
		}else {
			IndicadorHM_1.setBackground(Color.RED);
		}

		IndicadorHM_1.setBounds(403, 170, 54, 21);
		panel_8.add(IndicadorHM_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("OK");
		lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3_1.setBackground(Color.GREEN);
		lblNewLabel_3_1.setBounds(0, 0, 54, 21);
		IndicadorHM_1.add(lblNewLabel_3_1);
		
		Graph = new JPanel();
		Graph.setBounds(282, 45, 581, 426);
		contentPane.add(Graph);
		Graph.setLayout(new BoxLayout(Graph, BoxLayout.Y_AXIS));

        // Inicializar el ChartPanel para mostrar el gráfico
        chartPanel = new ChartPanel(null);
        chartPanel.setBackground(new Color(255, 255, 255));
        Graph.add(chartPanel);
        mostrarGrafico("Velocidad");
		
		JPanel Header = new JPanel();
		Header.setBackground(new Color(255, 255, 255));
		Header.setBounds(282, 0, 581, 45);
		contentPane.add(Header);
		Header.setLayout(null);
		
		BatImg = new JLabel("");
		BatImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Bat4.jpg"));
		BatImg.setBounds(10, 0, 64, 45);
		Header.add(BatImg);
		
		Velocidad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Velocidad");
				Optselected = "Velocidad";
				panel_6.setBackground(new Color(0,0,0));
				lblNewLabel_2.setForeground(new Color(255,255,255));
				panel_6_1.setBackground(new Color(160,222,137));
				
				panel_6_2.setBackground(Color.GRAY);
				lblNewLabel_2_1.setForeground(new Color(0, 0,0));
				panel_6_1_1.setBackground(new Color(192, 192, 192));
				
				panel_6_3.setBackground(Color.GRAY);
				lblNewLabel_2_2.setForeground(new Color(0, 0,0));
				panel_6_1_2.setBackground(new Color(192, 192, 192));
				
				panel_6_4.setBackground(Color.GRAY);
				lblNewLabel_2_3.setForeground(new Color(0, 0,0));
				panel_6_1_3.setBackground(new Color(192, 192, 192));
				
				panel_6_5.setBackground(Color.GRAY);
				lblNewLabel_2_4.setForeground(new Color(0, 0,0));
				panel_6_1_4.setBackground(new Color(192, 192, 192));
			}
		});
		
		Temperatura.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Temperatura");
				Optselected = "Temperatura";
				panel_6.setBackground(Color.GRAY);
				lblNewLabel_2.setForeground(new Color(0, 0,0));
				panel_6_1.setBackground(new Color(192, 192, 192));
				
				panel_6_2.setBackground(new Color(0,0,0));
				lblNewLabel_2_1.setForeground(new Color(255,255,255));
				panel_6_1_1.setBackground(new Color(160,222,137));
				
				panel_6_3.setBackground(Color.GRAY);
				lblNewLabel_2_2.setForeground(new Color(0, 0,0));
				panel_6_1_2.setBackground(new Color(192, 192, 192));
				
				panel_6_4.setBackground(Color.GRAY);
				lblNewLabel_2_3.setForeground(new Color(0, 0,0));
				panel_6_1_3.setBackground(new Color(192, 192, 192));
				
				panel_6_5.setBackground(Color.GRAY);
				lblNewLabel_2_4.setForeground(new Color(0, 0,0));
				panel_6_1_4.setBackground(new Color(192, 192, 192));
			}
		});
		
		Corriente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Corriente");
				Optselected = "Corriente";
				panel_6.setBackground(Color.GRAY);
				lblNewLabel_2.setForeground(new Color(0, 0,0));
				panel_6_1.setBackground(new Color(192, 192, 192));
				
				panel_6_2.setBackground(Color.GRAY);
				lblNewLabel_2_1.setForeground(new Color(0, 0,0));
				panel_6_1_1.setBackground(new Color(192, 192, 192));
				
				panel_6_3.setBackground(new Color(0,0,0));
				lblNewLabel_2_2.setForeground(new Color(255,255,255));
				panel_6_1_2.setBackground(new Color(160,222,137));
				
				panel_6_4.setBackground(Color.GRAY);
				lblNewLabel_2_3.setForeground(new Color(0, 0,0));
				panel_6_1_3.setBackground(new Color(192, 192, 192));
				
				panel_6_5.setBackground(Color.GRAY);
				lblNewLabel_2_4.setForeground(new Color(0, 0,0));
				panel_6_1_4.setBackground(new Color(192, 192, 192));
			}
		});
		
		Voltaje.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Voltaje");
				Optselected = "Voltaje";
				panel_6.setBackground(Color.GRAY);
				lblNewLabel_2.setForeground(new Color(0, 0,0));
				panel_6_1.setBackground(new Color(192, 192, 192));
				
				panel_6_2.setBackground(Color.GRAY);
				lblNewLabel_2_1.setForeground(new Color(0, 0,0));
				panel_6_1_1.setBackground(new Color(192, 192, 192));
				
				panel_6_3.setBackground(Color.GRAY);
				lblNewLabel_2_2.setForeground(new Color(0, 0,0));
				panel_6_1_2.setBackground(new Color(192, 192, 192));
				
				panel_6_4.setBackground(new Color(0,0,0));
				lblNewLabel_2_3.setForeground(new Color(255,255,255));
				panel_6_1_3.setBackground(new Color(160,222,137));
				
				panel_6_5.setBackground(Color.GRAY);
				lblNewLabel_2_4.setForeground(new Color(0, 0,0));
				panel_6_1_4.setBackground(new Color(192, 192, 192));
			}
		});
		
		Potencia.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarGrafico("Potencia");
				Optselected = "Potencia";
				panel_6.setBackground(Color.GRAY);
				lblNewLabel_2.setForeground(new Color(0, 0,0));
				panel_6_1.setBackground(new Color(192, 192, 192));
				
				panel_6_2.setBackground(Color.GRAY);
				lblNewLabel_2_1.setForeground(new Color(0, 0,0));
				panel_6_1_1.setBackground(new Color(192, 192, 192));
				
				panel_6_3.setBackground(Color.GRAY);
				lblNewLabel_2_2.setForeground(new Color(0, 0,0));
				panel_6_1_2.setBackground(new Color(192, 192, 192));
				
				panel_6_4.setBackground(Color.GRAY);
				lblNewLabel_2_3.setForeground(new Color(0, 0,0));
				panel_6_1_3.setBackground(new Color(192, 192, 192));
				
				panel_6_5.setBackground(new Color(0,0,0));
				lblNewLabel_2_4.setForeground(new Color(255,255,255));
				panel_6_1_4.setBackground(new Color(160,222,137));
			}
		});
	}
	

	public void updateData(Telemetria telemetria) {
        // Update labels with the new data
        velocidadLabel.setText(String.format("%.2f", telemetria.getVelocidad())+ " km/h");
        temperaturaLabel.setText(String.format("%.2f", telemetria.getTemperatura())+ " °C");
        corrienteLabel.setText(String.format("%.2f", telemetria.getCorriente())+ " A");
        voltajeLabel.setText(String.format("%.2f", telemetria.getVoltaje())+ " V");
        PotenciaLabel.setText(String.format("%.2f", telemetria.getPotencia())+ " W");
        ValDist.setText(String.format("%.2f", telemetria.getDistanciaRecorrida())); 
        VelProm.setText(String.format("%.2f", telemetria.getDistanciaRecorrida()));
        PotProm.setText(String.format("%.2f", telemetria.getPotenciaPromedio()));

		String path = System.getProperty("user.dir");
        
        updateGlobalVarbalVar(Optselected);
		if(telemetria.getTemperatura()<35.0) {
			IndicadorHM_1.setBackground(Color.GREEN);
		}else {
			IndicadorHM_1.setBackground(Color.RED);
		}
		
		if(telemetria.getHombreMuerto()) {
			IndicadorHM.setBackground(Color.GREEN);
		}else {
			IndicadorHM.setBackground(Color.RED);
		}
		
		if(telemetria.getVoltaje()<47.4) {
			BatImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Bat1.jpg"));
		}else if(telemetria.getVoltaje()<48.9) {
			BatImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Bat2.jpg"));
		} else if(telemetria.getVoltaje()<50.4) {
			BatImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Bat3.jpg"));
		} else {
			BatImg.setIcon(new ImageIcon(path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Bat4.jpg"));
		}
		BatImg.revalidate();
		BatImg.repaint();
    
        repaint();
    }
	
	
    public void actualizarDatosTelemetria() {
    	long currentTime = System.currentTimeMillis() - startTime;  // Tiempo transcurrido en milisegundos
        double timeInSeconds = currentTime / 1000.0; 
        velocidadSeries.add(timeInSeconds, telemetria.getVelocidad());
        temperaturaSeries.add(timeInSeconds, telemetria.getTemperatura());
        corrienteSeries.add(timeInSeconds, telemetria.getCorriente());
        voltajeSeries.add(timeInSeconds, telemetria.getVoltaje());
        potenciaSeries.add(timeInSeconds, telemetria.getPotencia());
        TiempoMin.setText(String.format("%.2f", timeInSeconds/60));
        
    }
	
	
    private void mostrarGrafico(String tipo) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        Color lineColor = Color.BLACK;  // Color por defecto
        Color purple = new Color(128, 0, 128); // Purple

        switch (tipo) {
            case "Velocidad":
                dataset.addSeries(velocidadSeries);
                lineColor = Color.RED;
                break;
            case "Temperatura":
                dataset.addSeries(temperaturaSeries);
                lineColor = purple; 
                break;
            case "Corriente":
                dataset.addSeries(corrienteSeries);  
                break;
            case "Voltaje":
                dataset.addSeries(voltajeSeries);
                lineColor = Color.BLUE; 
                break;
            case "Potencia":
                dataset.addSeries(potenciaSeries);
                lineColor = Color.MAGENTA; 
                break;
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                tipo + " vs Tiempo",
                "Tiempo (s)",
                tipo,
                dataset
        );

        // Personalizar la línea del gráfico
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, lineColor);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); 
        renderer.setSeriesShapesVisible(0, false); 
        plot.setRenderer(renderer);

        chartPanel.setChart(chart);
        Graph.revalidate();  
        Graph.repaint();
    }
    
	public void updateGlobalVarbalVar(String tipo) {
		switch (tipo) {
        case "Velocidad":
        	VariableSel.setText(telemetria.getVelocidad()+ " km/h");
            break;
        case "Temperatura":
        	VariableSel.setText(telemetria.getTemperatura()+ " °C");
            break;
        case "Corriente":
        	VariableSel.setText(telemetria.getCorriente()+ " A");
            break;
        case "Voltaje":
        	VariableSel.setText(telemetria.getVoltaje()+ " V");
            break;
        case "Potencia":
        	VariableSel.setText(telemetria.getPotencia()+ " W");
            break;
    }
	}
	
	private void actualizarDatosVehiculo(Vehiculo vehiculo) {
	    Nombrelbl.setText(vehiculo.getNombre());
	    NumRuedaslbl.setText(String.valueOf(vehiculo.getNumDeRuedas()));
	    pesolbl.setText(String.valueOf(vehiculo.getPeso()));
	    Categorialbl.setText(vehiculo.getCategoria());
	    Pilotolbl.setText(vehiculo.getPiloto());

	    // Actualiza la imagen
	    CarImg.setIcon(new ImageIcon(vehiculo.getImagen()));
	}
	
	private void limpiarDatosVehiculo() {
	    Nombrelbl.setText("");
	    NumRuedaslbl.setText("");
	    pesolbl.setText("");
	    Categorialbl.setText("");
	    Pilotolbl.setText("");
	    CarImg.setIcon(null); 
	}
}
