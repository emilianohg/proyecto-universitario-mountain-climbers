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

        initScenes();
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        int w = 51, h = 51;

        for (int i = 0; i < 10; i++) {
            scene.addTile(new Tile("src/assets/tiles/grass.png", i*w, 400 - h));
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                scene.addTile(new Tile("src/assets/tiles/rock.png", j * w, i * h + 400));
            }
        }

        for (int i = 0; i < 10; i++) {
            scene.addTile(new Tile("src/assets/tiles/grass.png", i*w, getHeight() - h));
        }

        GameCanvas gameCanvas = new GameCanvas(getWidth() / games.length, getHeight());

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

        Player player = new Player("src/assets/players/player_"+number+"/Idle__000.png");
        player
            .setHeightMaintainScale(100)
            .setCoordinates(100, getHeight() - player.getHeight() - 45);

        player.addAnimations(new PlayerAnimation[] { animationClimb, animationAttack, animationIdle });

        gameCanvas.addPlayer(player);

        add(gameCanvas);

        games[number - 1] = gameCanvas;
    }

}
