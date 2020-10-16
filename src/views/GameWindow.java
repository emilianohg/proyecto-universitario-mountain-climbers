package views;

import domain.*;

import javax.swing.*;
import java.awt.*;

import static utils.StringUtils.getUrls;

public class GameWindow extends JFrame {

    GameCanvas[] games;
    int numberPlayers = 4;

    public GameWindow () {
        super("Mountain climbers");

        setResizable(false);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        setVisible(true);
        initScenes();
        initGlass();

        revalidate();
        repaint();
        update(getGraphics());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGlass () {
        JPanel glass = (JPanel) getGlassPane();
        glass.setBackground(Color.RED);
        glass.setSize(getWidth(), getHeight());
        glass.add(new JButton("Hola mundo"));
        glass.setVisible(true);
        glass.update(glass.getGraphics());
    }

    private void initScenes () {
        games = new GameCanvas[numberPlayers];
        for (int i = 1; i <= numberPlayers; i++) {
            addGameCanvas(i);
        }
    }

    private void addGameCanvas (int number) {
        SceneLayout[] layoutsScene = new SceneLayout[] {
                new SceneLayout("src/assets/scenes/game_background_"+number+"/layers/sky.png"),
                new SceneLayout("src/assets/scenes/game_background_"+number+"/layers/clouds_1.png"),
                new SceneLayout("src/assets/scenes/game_background_"+number+"/layers/clouds_2.png"),
        };
        Scene scene = new Scene(layoutsScene);

        int tileWidth = 51, tileHeight = 51;

        for (int i = 0; i < 10; i++) {
            scene.addTile(new Tile("src/assets/tiles/grass.png", i*tileWidth, 400 - tileHeight));
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                scene.addTile(new Tile("src/assets/tiles/rock.png", j * tileWidth, i * tileHeight + 400));
            }
        }

        for (int i = 0; i < 10; i++) {
            scene.addTile(new Tile("src/assets/tiles/grass.png", i*tileWidth, getHeight() - tileHeight));
        }

        int w = getContentPane().getWidth();
        int h = getContentPane().getHeight();

        System.out.println(w);
        System.out.println(h);

        GameCanvas gameCanvas = new GameCanvas( w / games.length, h);

        gameCanvas.addScene(scene);

        PlayerAnimation animationIdle = new PlayerAnimation(
            "idle",
            getUrls("src/assets/players/player_"+number+"/Idle__%03d.png", 9),
            200
        );
        PlayerAnimation animationClimb = new PlayerAnimation(
            "climb",
            getUrls("src/assets/players/player_"+number+"/Climb_%03d.png", 9),
            200
        );
        PlayerAnimation animationAttack = new PlayerAnimation(
                "attack",
                getUrls("src/assets/players/player_"+number+"/Attack__%03d.png", 9),
                200
        );
        PlayerAnimation animationJump = new PlayerAnimation(
                "jump",
                getUrls("src/assets/players/player_"+number+"/Jump__%03d.png", 9),
                200
        );

        Player player = new Player("src/assets/players/player_"+number+"/Idle__000.png");
        player
            .setHeightMaintainScale(100)
            .setCoordinates(100, getHeight() - player.getHeight() - 45);

        player.addAnimations(new PlayerAnimation[] {
            animationIdle,
            animationClimb,
            animationAttack,
            animationJump,
        });
        player.setAnimation("idle");

        gameCanvas.addPlayer(player);

        add(gameCanvas);

        games[number - 1] = gameCanvas;
    }

}
