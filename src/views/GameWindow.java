/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package views;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.lang.Thread.sleep;
import static utils.StringUtils.getUrls;

public class GameWindow extends JFrame implements GameListener {

    GameCanvas[] games;
    int numberPlayers = 4;
    int totalGamesLoaded = 0;

    JGameButton btnStart, btnRestart;
    Game game;
    JPanel glass;

    JLabel loadingLabel;

    public GameWindow () {
        super("Mountain climbers");

        setResizable(false);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        setVisible(true);
        initGlass();

        initScenes();

        update();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGlass () {
        glass = (JPanel) getGlassPane();
        glass.setLayout(new GridBagLayout());

        btnStart = new JGameButton("src/assets/buttons/play-medium.png", "START");
        btnStart.addActionListener(this::startGame);
        btnStart.setVisible(false);

        btnRestart = new JGameButton("src/assets/buttons/replay.png", "RETURN");
        btnRestart.addActionListener(this::restartGame);
        btnRestart.setVisible(false);

        loadingLabel = new JLabel("Loading ...");
        loadingLabel.setFont(new Font("Courier", Font.BOLD, 36));

        glass.add(btnStart);
        glass.add(btnRestart);
        glass.add(loadingLabel);
        glass.update(glass.getGraphics());
        glass.setOpaque(false);
        glass.setVisible(true);
        glass.update(glass.getGraphics());
    }

    private void restartGame(ActionEvent actionEvent) {
        btnRestart.setVisible(false);

         for (GameCanvas game : games) {
            game.restart();
         }
    }

    private void startGame(ActionEvent actionEvent) {
        btnStart.setVisible(false);

        for (GameCanvas game : games) {
            game.start();
        }
    }

    private void initScenes () {
        games = new GameCanvas[numberPlayers];
        game = Game.getInstance(numberPlayers);
        game.addListener(this);
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
            scene.addTile(new Tile("src/assets/tiles/grass-"+number+".png", i*tileWidth, 400 - tileHeight));
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                scene.addTile(new Tile("src/assets/tiles/rock-"+number+".png", j * tileWidth, i * tileHeight + 400));
            }
        }

        for (int i = 0; i < 10; i++) {
            scene.addTile(new Tile("src/assets/tiles/grass-"+number+".png", i*tileWidth, getHeight() - tileHeight));
        }

        int w = getContentPane().getWidth();
        int h = getContentPane().getHeight();

        GameCanvas gameCanvas = new GameCanvas( w / games.length, h, number);

        gameCanvas.addScene(scene);

        PlayerAnimation animationIdle = new PlayerAnimation(
            "idle",
            getUrls("src/assets/players/player_"+number+"/Idle__%03d.png", 9)
        );
        PlayerAnimation animationGlide = new PlayerAnimation(
                "glide",
                getUrls("src/assets/players/player_"+number+"/Glide_%03d.png", 9)
        );
        PlayerAnimation animationClimb = new PlayerAnimation(
            "climb",
            getUrls("src/assets/players/player_"+number+"/Climb_%03d.png", 9)
        );
        PlayerAnimation animationAttack = new PlayerAnimation(
                "attack",
                getUrls("src/assets/players/player_"+number+"/Attack__%03d.png", 9),
                10
        );
        PlayerAnimation animationJump = new PlayerAnimation(
                "jump",
                getUrls("src/assets/players/player_"+number+"/Jump__%03d.png", 9),
                10
        );

        Player player = new Player("src/assets/players/player_"+number+"/Idle__000.png");
        player.setHeightMaintainScale(100);

        player.addAnimations(new PlayerAnimation[] {
            animationIdle,
            animationGlide,
            animationClimb,
            animationAttack,
            animationJump,
        });
        player.setAnimation("idle");

        gameCanvas.addPlayer(player);
        add(gameCanvas);

        games[number - 1] = gameCanvas;
    }

    @Override
    public void notifyEndGame () {
        btnRestart.setVisible(true);
        update();
    }

    @Override
    public void notifyReset() {
        btnStart.setVisible(true);
    }

    @Override
    public void notifyNewGameLoad() {
        totalGamesLoaded++;
        if (totalGamesLoaded == numberPlayers) {
            loadingLabel.setVisible(false);

            for (GameCanvas game : games) {
                game.setVisible(true);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            new Thread(() -> {
                try {
                    sleep(2000);
                    btnStart.setVisible(true);
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void update() {
        revalidate();
        repaint();
        update(getGraphics());
    }
}
