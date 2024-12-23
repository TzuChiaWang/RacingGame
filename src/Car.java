import java.awt.*;

public class Car {

    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;
    private int moveDistance;

    public Car(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = 60;
        this.height = 115;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void moveLeft() {
        if (x > 100) { // 限制在賽道範圍內
            x -= 10;
        }
    }

    public void moveRight() {
        if (x < 400) { // 500 (panel width) - 100 (賽道右邊界)
            x += 10;
        }
    }

    public void moveDown(int speed) {
        y += speed;
    }

    public boolean intersects(Car other) {
        // 縮小邊界以降低碰撞檢測的敏感度
        int shrinkAmount = 2; // 可以根據需要調整
        Rectangle r1 = new Rectangle(x + shrinkAmount, y + shrinkAmount, width - 2 * shrinkAmount,
                height - 2 * shrinkAmount);
        Rectangle r2 = new Rectangle(other.x + shrinkAmount, other.y + shrinkAmount, other.width - 2 * shrinkAmount,
                other.height - 2 * shrinkAmount);
        return r1.intersects(r2);
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void KeymoveLeft() {
        if (x > 100) { // 限制在賽道範圍內
            x -= moveDistance;
            if (x < 100) {
                x = 100; // 確保不超出左邊界
            }
        }
    }

    public void KeymoveRight() {
        if (x < 400) { // 500 (panel width) - 100 (賽道右邊界)
            x += moveDistance;
            if (x > 350) {
                x = 350; // 確保不超出右邊界
            }
        }
    }

    public void setMoveDistance(int distance) {
        this.moveDistance = distance;
    }
}
