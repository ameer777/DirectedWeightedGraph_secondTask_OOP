package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class loginToGame implements ActionListener {

    private static boolean _open = true;
    private static int _scenario = 0;
    private static long _id = -1;
    private static JComboBox _levelsNumver;
    private static JButton _startGame;
    private static JButton _loginId;
    private static JTextField _userTxt;
    private static JFrame jf;

    public void loginToGame()  {

        JPanel panel = new JPanel();
        jf = new JFrame();
        jf.setSize(400,250);
        panel.setLayout(null);
        jf.add(panel);
        JLabel user = new JLabel("user:");
        user.setBounds(10,20,80,25);

        JLabel scene = new JLabel("Scenario:");
        scene.setBounds(10,50,80,25);

        panel.add(user);
        panel.add(scene);

        _userTxt = new JTextField(20);
        _userTxt.setBounds(100,20,165,25);

        String scenes[] = new String[24];
        for(int i = 0; i < 24; i++){
            scenes[i] = String.valueOf(i);
        }
        _levelsNumver = new JComboBox(scenes);
        _levelsNumver.addActionListener(new loginToGame());
        _levelsNumver.setBounds(108,50,165,25);

        _startGame = new JButton("start game");
        _startGame.addActionListener(new loginToGame());
        _startGame.setBounds(25,100,120,30);

        _loginId = new JButton("login");
        _loginId.addActionListener(new loginToGame());
        _loginId.setBounds(250,100,120,30);

        panel.add(user);
        panel.add(scene);
        panel.add(_userTxt);
        panel.add(_levelsNumver);
        panel.add(_startGame);
        panel.add(_loginId);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }


    public void dispose(){
        jf.dispose();
    }

    public boolean isOpen() {
        return _open;
    }

    public long getID(){
        return _id;
    }

    public int getScenario(){
        return _scenario;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _levelsNumver){

            int scenario = _levelsNumver.getSelectedIndex();
            _scenario = scenario;
        }
        if(e.getSource() == _loginId){
                long id = Integer.parseInt(_userTxt.getText());
                _id = id;

        }
        Thread client = new Thread(new Ex2());
        if(e.getSource() == _startGame){
            _open = false;
            dispose();
            client.start();
        }

    }
}
