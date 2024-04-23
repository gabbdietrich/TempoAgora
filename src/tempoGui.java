import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class tempoGui extends JFrame {
    private JSONObject weatherData;
    public tempoGui() {

        super("Previsão do Tempo ");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(450, 650);

        setLocationRelativeTo(null);

        setLayout(null);

        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField();

        searchTextField.setBounds(15, 15, 351, 45);

        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(searchTextField);

        //botão de pesquisa

        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        //texto de temperatura
        JLabel temperaturaText = new JLabel("10 c");
        temperaturaText.setFont(new Font("Dialog", Font.BOLD, 48));

        temperaturaText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperaturaText);

        JLabel weatherConditionDesc = new JLabel("Nublado");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 33));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Umidade</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 12)); //estilizando a font e escrita

        JLabel windspeedImage = new JLabel(loadImage("src/assets/wind_5628744.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b>Velocidade do vento </b> 15km/h</html>");
        windspeedText.setBounds(330, 500, 85, 65);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 12));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userInput = searchTextField.getText();


                if (userInput.replaceAll("\\s", "").length() <= 0){
                    return;
                }

//             weatherData = PrevisaoApp.getPrevisaoTempoData(userInput);

                String weatherCondition = (String) weatherData.get("weather_condition");

                switch (weatherCondition){
                    case "Clear":
                     weatherConditionImage.setIcon(loadImage("src/assets/clear.png")) ;
                     break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png")) ;
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png")) ;
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png")) ;
                        break;
                }
                double temperature = (double) weatherData.get("temperature");
                temperaturaText.setText(temperature + " C");

                weatherConditionDesc.setText(weatherCondition);

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");


                double windspeed = (long) weatherData.get("windspeed");
                windspeedText.setText("<html><b>Humidity</b> " + windspeed + "km/h</html>");
            }
        });
        add(searchButton);

    }

    private ImageIcon loadImage(String resourcePath) {
        try {

            BufferedImage image = ImageIO.read(new File(resourcePath));

            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Recurso não encontrado: ");
        return null;
    }

}
