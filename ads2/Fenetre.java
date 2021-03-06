import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * classe representant la fenetre graphique ,c'est ici que tous les composants sont stockés.
 */



public class Fenetre extends JFrame{
  
	
    private JTabbedPane onglet;
    ArrayList<String> titre = new ArrayList<String>();
    private JPanel container =new JPanel() ;
    private JMenuBar bar = new JMenuBar();
    private JMenu menu = new JMenu("Fichier") ;
    private JMenu menu2 = new JMenu("sound") ;
    private JMenuItem item = new JMenuItem("Enregistrer sous");
    private JMenuItem item2 = new JMenuItem("Ouvrir un document existant");
    private JMenuItem item4 = new JMenuItem("Quitter");
    private JMenuItem item6 = new JMenuItem("Créer un nouveau document");
    private JMenuItem item7 = new JMenuItem("supprimer un onglet");
    private JMenuItem item8 = new JMenuItem("EXECUTER");
    private JMenuItem item9 = new JMenuItem("RE - initialiser");
    private JMenuItem item10 = new JMenuItem("STOPPER L'action");
    private JMenuItem item11 = new JMenuItem("SOUND PARAMETER"); 
    private JMenuItem item12 = new JMenuItem("sound off"); 
    private JMenuItem item13 = new JMenuItem("sound on"); 
    public Main main ;
    public Thread exe ;
    ArrayList<Float> inter = new ArrayList<Float>() ;
    ArrayList<ConteneurC> inter2 = new ArrayList<ConteneurC>() ;
    int inc =0 ;
    int inc2=0 ;
	int vit = 120 ;

    
    Panneau p = new Panneau(this);
    /*
     * Gestion des onglets 
     */
    private Memoire memoire = new Memoire() ;
    
    Errore err= new Errore() ;
    //COnsole du bas pour la gestion des exceptions
    JTextField jtf ;
    
    Brui [] brui = new Brui[10];
    boolean bruitage = false ;
    Sequencer sequencer ;
	boolean debut =false;
    
    
    
 	public void annule(){
		for(int i = 0 ; i< brui.length ; i++){
			if(brui[i]!= null){
				brui[i].stop() ;
			}
		}
	}
	
    public Fenetre(){
	setTitle("LUNAR");
	//setSize(1000,650);
	
	setSize(1000,700);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	container.setBackground(Color.WHITE);
	container.setLayout(new BorderLayout());
	jtf = new JTextField();
	Font police = new Font("Arial", Font.BOLD, 14);
	jtf.setFont(police);
	JPanel south = new JPanel();
	

		JSlider slide = new JSlider();
		slide.setMaximum(130);
		slide.setMinimum(0);
		slide.setValue(120);
		slide.setPaintTicks(true);
		slide.setPaintLabels(true);
		slide.setMinorTickSpacing(10);
		slide.setMajorTickSpacing(20);
		slide.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent event){
			
			vit= ((JSlider)event.getSource()).getValue();
			
		}
		});
	
	
	err.setPreferredSize(new Dimension(750, 40));
	south.add(slide);
	south.add(new JScrollPane(err));
	container.add(south , BorderLayout.SOUTH);
		
	setContentPane(container);
	onglet = new JTabbedPane();
		
	/*
	 * Sauvegarde d'un fichier
	 */
	item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
	menu.add(item); 
	item.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    try{
			new FileWriter(new File(titre.get(onglet.getSelectedIndex()))).close();
		    }catch (IOException e) {
			e.printStackTrace();
		    }
		    File file= new File(titre.get(onglet.getSelectedIndex()));
		    JTextArea tmp =(JTextArea)(onglet.getSelectedComponent());
	    		
		    memoire.ecrire(titre.get(onglet.getSelectedIndex()) , tmp.getText());
	    	
		}
	    });
	
		
	/*
	 * Ouvrir un nouvel onglet
	 */
	menu.add(item6);
	item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_MASK));
	item6.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		    String nom = jop.showInputDialog(null, "Nom du fichier : ",
						     "Information",
						     JOptionPane.QUESTION_MESSAGE);
	  			
		
	    		
		    titre.add(nom);
	  			
		    JTextArea text = new JTextArea() ;
		    text.setCaretColor(Color.WHITE);
		    text.setTabSize(2);
		    text.setLineWrap(true);
		    text.setDragEnabled(true);
		    Font police = new Font("Arial", Font.BOLD, 15);
		    text.setFont(police);
		    text.setBackground(new Color(15, 5, 107));
		    text.setForeground(Color.WHITE);
			   
		    onglet.add(nom ,text);
		    brui[8] = new Brui("music/select",true);
		    brui[8].jou();
		    
		    p.niveau=1;
		   
		    if(!debut){
		    	debut=true ;
		    	 sequencer.stop();
		    try { 
				  Sequence sequence = MidiSystem.getSequence(new File("music2/Jeu.mid" )); 
				  sequencer = MidiSystem.getSequencer(); 
				  sequencer.open(); 
				  sequencer.setSequence(sequence); 
				  sequencer.setLoopCount(100);
				  sequencer.start();
				  
				} catch (Exception e) {} 
	  			p.repaint() ;
		    }
		}
	    });
		
	/*
	 * Fermer l'onglet courant
	 */
	item7.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,KeyEvent.CTRL_MASK));
	menu.add(item7);
	item7.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){			
		    int selected = onglet.getSelectedIndex();
		    titre.remove(onglet.getSelectedIndex());
		    if(selected > -1)onglet.remove(selected);
		}
	    });
		
	/*
	 * Ouvrir un fichier qui existe deja 
	 */
	item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_MASK));
	menu.add(item2);
	item2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
		    JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		    String name = jop.showInputDialog(null, "Nom du fichier : ",
						      "Information",
						      JOptionPane.QUESTION_MESSAGE);
		    JTextArea text = new JTextArea() ;
		    text.setTabSize(2);
		    text.setCaretColor(Color.WHITE);
		    text.setBackground(new Color(15, 5, 107));
		    text.setForeground(Color.WHITE);
		    text.setDragEnabled(true);
		    Font police = new Font("Arial", Font.BOLD, 15);
		    text.setFont(police);
			   
	  		
		    String str = memoire.af(name);
		    text.setText(str);
		    onglet.add(name,text);
		    titre.add(name);
		    brui[8] = new Brui("music/select",true);
		    brui[8].jou();
		    p.niveau=1;
		    
		    sequencer.stop();
		    try { 
		    	Sequence sequence =null;
		    	if(name.equals("maison.ln")){
				  sequence = MidiSystem.getSequence(new File("music2/Home.mid" )); 
		    	}else if(name.equals("triforce.ln")){
		    		sequence = MidiSystem.getSequence(new File("music2/Ombre.mid" )); 
		    	}else if(name.equals("spiral.ln")){
		    		sequence = MidiSystem.getSequence(new File("music2/Fee.mid" )); 
		    	}else if(name.equals("triangle.ln")){
		    		sequence = MidiSystem.getSequence(new File("music2/Chateau.mid" )); 
		    	}else if(name.equals("exempleProjet.ln")){
		    		sequence = MidiSystem.getSequence(new File("music2/Plaine.mid" )); 
		    	}else if(name.equals("carre.ln")){
		    		sequence = MidiSystem.getSequence(new File("music2/Maison.mid" )); 
		    	}else if(name.equals("ali")){
		    		sequence = MidiSystem.getSequence(new File("music2/Donjon1.mid" )); 
		    	}else if(name.equals("ali2")){
		    		sequence = MidiSystem.getSequence(new File("music2/Donjon2.mid" )); 
		    	}else{
		    		sequence = MidiSystem.getSequence(new File("music2/Foret.mid" )); 
		    	}
		    	
				  sequencer = MidiSystem.getSequencer(); 
				  sequencer.open(); 
				  sequencer.setSequence(sequence); 
				  sequencer.setLoopCount(100);
				  sequencer.start();
				  
				} catch (Exception e) {} 
		    p.repaint();
		}
	    });
	menu.add(item8);
		
	item8.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK));

	//Executer une tache 
	item8.setForeground(Color.BLUE);
	item8.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
			
			
	  		//sauvegarde du fichier
			if(titre.contains(titre.get(onglet.getSelectedIndex()))){
				
			try {
				new FileWriter(new File(titre.get(onglet.getSelectedIndex()))).close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			
		
				    File file= new File(titre.get(onglet.getSelectedIndex()));
				    JTextArea tmp =(JTextArea)(onglet.getSelectedComponent());
			    		String tempo =titre.get(onglet.getSelectedIndex()) ;
				    memoire.ecrire(titre.get(onglet.getSelectedIndex()) , tmp.getText());
				    
				    try {
				    
						main = new Main(titre.get(onglet.getSelectedIndex())) ;
						
					    
					    err.setTexte(main.Err);
					    if(main.Err == ""){
					    	 err.setTexte("Le code est Correcte");
					    	 exe = new Thread(new Exec());
							 exe.start();
							   brui[7] = new Brui("music/select2",true);
								brui[7].jou();
					    }
					} catch (Exception e) {
						
						e.printStackTrace();
					
					}
				 
				  
		}
	    });
		
	item9.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK));
		menu.add(item9);
		item9.setForeground(Color.RED);
		item9.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				restart() ;
				
			}
    	});
		
		item11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
		menu.add(item11);
		item11.setForeground(Color.GREEN);
		item11.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				JOptionPane az = new JOptionPane() ;
	  			int opt = az.showConfirmDialog(null ,"activer le son ?","Menu",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	  			 if( opt == JOptionPane.OK_OPTION){
	  				 bruitage =true;   
	  				 brui[0] = new Brui("music/run",true);
	  				brui[1] = new Brui("music/cercle",true);
	  				brui[2] = new Brui("music/cri",true);
	  				brui[3] = new Brui("music/MC_Error",true);
	  				sequencer.start() ;
	  					
	  			 }else{
	  				 bruitage=false;
	  			    brui[0] = new Brui("music/run",false);
	  				brui[1] = new Brui("music/cercle",false);
	  				brui[2] = new Brui("music/cri",false);
	  				brui[3] = new Brui("music/MC_Error",false);
	  				sequencer.stop() ;
	  					
	  			 }
			}
    	});
		
	menu2.add(item11);
	
	
	item12.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_MASK));
	menu.add(item12);
	item12.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event){
			brui[1].setBool(false);
			brui[0].setBool(false);
		}
	});
	
	
		menu2.add(item13);
		item13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,KeyEvent.CTRL_MASK));
		
		item13.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				 brui[0] = new Brui("music/run",true);
	  			brui[1] = new Brui("music/cercle",true);
			}
		});
		
		menu2.add(item12);
		
		
		bar.add(menu);
		bar.add(menu2);
		
	JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,  new JScrollPane(onglet),new JScrollPane(p));
	//On place le séparateur
	split.setDividerLocation(300);
	    

	  
	  
	//On ajoute le tout
	this.getContentPane().add(split, BorderLayout.CENTER);
	
	setJMenuBar(bar);
	setVisible(true);
	
	try { 
		  Sequence sequence = MidiSystem.getSequence(new File("music2/Titre.mid" )); 
		  sequencer = MidiSystem.getSequencer(); 
		  sequencer.open(); 
		  sequencer.setSequence(sequence); 
		  sequencer.setLoopCount(100);
		  sequencer.start();
		  
		} catch (Exception e) {} 
	
		
	 
    brui[0] = new Brui("music/run",false);
	brui[1] = new Brui("music/cercle",false);
	brui[2] = new Brui("music/cri",false);
	brui[3] = new Brui("music/MC_Error",false);
	brui[4] = new Brui("music/fin",false);

	/*
	//ceci est le bruitage (deplacement de la tortue rotation
	brui[0] = new Brui("music/run",true);
	brui[1] = new Brui("music/cercle",true);
	brui[2] = new Brui("music/cri",true);
	brui[3] = new Brui("music/MC_Error",true);
	brui[4] = new Brui("music/fin",true);
	 */  
	debut() ;
    }
    
    public void debut(){
    	for(int i=0;i< 30;i++){
    		p.y2+=10 ;
    		p.pause(200);
    		p.repaint();
    	}
    }
	public void restart(){
		p.line = new ArrayList<LineT>();
		  p.x = 0 ;
		    //	int y = 550;
		     p.y =0 ;
		     p.angular =0 ;
		     p.pince =false;
		    p.pincea = new ArrayList<Float>() ;
		    //epaisseur de la taille du crayon
		    float ep =3;
		    
		    p.depart =0 ;
		    p.depart2 =0 ;
		    p.line = new ArrayList<LineT>();
			
			
			
		    p.orientation =0 ;
		    p.avance = 1 ;
			
			
		    p.affiche =false;
		    p.affiche2 =false;
		    p.anglerot =0 ;
		    p.repaint();
	}
    
    public void avance(int nb){
      	brui[0].lop();
  
    
	//	p.orientation=or;
    	LineT li = new LineT();
    	li.x = p.x ;
    	li.y=550 - p.y ;
    	p.ii=li.x ;
    	p.ii2=li.y ;
	int dX=(int)(nb*Math.cos(Math.toRadians(p.angular)));
	int dY=(int)(nb*Math.sin(Math.toRadians(p.angular)));
		
	//System.out.println(dX+"  "+dY);
	p.avance =0 ;
			
	int xx =p.x ;
	int yy =p.y;
		
	li.taille = inter.get(inc);
	li.cont = inter2.get(inc);
	inc++;
	

			
		
		
	int xx2 = 0;
	
		
	if(p.angular>=0 && p.angular <90){	
	    p.orientation=0;
		
			
	    for(int i=0 ; i< (int)(nb/10) ;i++){
		p.x = p.x+(int)(10*Math.cos(Math.toRadians(p.angular)));
				
		p.y = p.y+ (int)(10*Math.sin(Math.toRadians(p.angular)));
				
				
				
		p.avance++ ;
						
		p.pause(vit);
		p.repaint();
		if(p.avance==4){
		    p.avance =1;
		}
	    }
	}else if(p.angular>=90 && p.angular<180){
			
		
	    p.orientation=3;
	    for(int i=0; i< (int)(nb/10) ;i++){
		p.x = p.x+(int)(10*Math.cos(Math.toRadians(p.angular)));
				
		p.y = p.y+ (int)(10*Math.sin(Math.toRadians(p.angular)));
		p.avance++ ;
		
		p.pause(vit);
		p.repaint();
		if(p.avance==4){
		    p.avance =1;
		}
	    }
	}
	else if(p.angular>= 180 && p.angular <270){
	    p.orientation=1;
	    for(int i=0; i< (int)(nb/10) ;i++){
		p.x = p.x+(int)(10*Math.cos(Math.toRadians(p.angular)));
				
		p.y = p.y+ (int)(10*Math.sin(Math.toRadians(p.angular)));
		p.avance++ ;
						
		p.pause(vit);
		p.repaint();
		if(p.avance==4){
		    p.avance =1;
		}
	    }
					
	}
	else if(p.angular>= 270 && p.angular <360){
	    p.orientation=2;
	    for(int i=0; i< (int)(nb/10) ;i++){
		p.x = p.x+(int)(10*Math.cos(Math.toRadians(p.angular)));
				
		p.y = p.y+ (int)(10*Math.sin(Math.toRadians(p.angular)));
		p.avance++ ;
					
		p.pause(vit);
		p.repaint();
		if(p.avance==4){
		    p.avance =1;
		}
	    }
	}
		
	p.x =xx;
	p.y=yy ;
		
	p.x+=dX;
	p.y	+=dY;
		
	li.x1=p.x ; 
	li.y1=p.y ;
	
	
	if(p.pince)
	p.line.add(li);
		
		brui[0].stop();
    }
    //a modifier
    public void tourne (int angular){
    	brui[1].lop();
    //	brui[2].jou();
    	p.avance =1;
    	p.orientation = 4 ;
    	p.affiche=true;
    	p.anglerot =angular;
    	for(int i =1 ; i< 16 ; i++){
	    p.avance++ ;
	    p.repaint() ;
	    p.pause(10);
    	}
    	p.affiche=false;
    	p.angular = p.angular + angular ;
    	if(p.angular>=360) p.angular =0 ;
    	if(p.angular<0) p.angular =270;
    	brui[1].stop();
    }
    
    
    /*
     * Ici c'est un Thread qui va executer le programme ecrit
     */
    class Exec implements Runnable{
   
    	 Brui br ;
    	
    	public void run() {
    		
    	
    		
    		//brui[3] = new Brui("music/AOL_Sword",true);
    		
    		for(int l=0 ; l< main.parser.av.cat.size() ;l++){
    			if(main.parser.av.cat.get(l)){
    				main.parser.av.epais.set(l,-1f);
    				main.parser.av.cont.set(l, null);
    			}
    		}
    		
    		for(int l =0 ; l< main.parser.av.epais.size() ; l++){
    			if(main.parser.av.epais.get(l)==-1f){
    				main.parser.av.epais.remove(l);
    			}
    			if(main.parser.av.cont.get(l)==null){
    				main.parser.av.cont.remove(l);
    			}
    		}
    		inter=main.parser.av.epais ;
    		
    		inter2=main.parser.av.cont ;
    	
    		p.pincea =main.parser.av.epais ;
    		
    	
    		 brui[5] = new Brui("music/baspinceau",true);
    		 brui[6] = new Brui("music/shield",true);
    		
    		 boolean prec = false;
    	for	(int i = 0 ; i< main.parser.av.ins.size() ; i++){
    		p.pince = main.parser.av.ins2.get(i) ;
    		if(prec != p.pince) {prec = p.pince ; if(prec) brui[5].jou() ; else brui[6].jou();}
    		
    		
    		if(main.parser.av.cat.get(i)){
    		  	
    				tourne(main.parser.av.ins.get(i));
    			
    		}else{
    			
    			avance(main.parser.av.ins.get(i));
    			
    		}
    		

    		if(p.x<-2 || p.x> 630 || p.y<= -2 || p.y >610) {
    			
				System.out.println("Exception: Vous etes sortie du cadran !");
				p.affiche2=true ;
				p.repaint() ;
				break ;
    		}
    			
    	}
    	main.parser.av.ins = new ArrayList<Integer>() ;
    	main.parser.av.ins2 = new ArrayList<Boolean>() ;
    	main.parser.av.cat = new ArrayList<Boolean>() ;
    	main.parser.av.epais = new ArrayList<Float>() ;	
    	main.parser.av.cont = new ArrayList<ConteneurC>();
    	
    	inc =0 ;
    	
    	
    	brui[4].jou();
    	}
    }
    
    
    public static void main(String[]ars){
    	new Fenetre();
    
    }
}

