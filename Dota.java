public class Dota {
    public static void main(String[] args) {
        Unit hero = new Hero(1500, 10, 10);
        Unit creep = new Creep(500, 8, 8);
        Unit creep2 = new Creep(500, 8, 8);
        Unit creep3 = new Creep(500, 8, 8);
        Unit creep4 = new Creep(500, 8, 8);
        Tower t3 = new EnhancedTower(170, 16, 15, 15);
        Tower t4 = new EnhancedTower(174, 21, 15, 15);
        Tower t1 = new Tower(100, 10, 15, 15);
        Unit[] units = {hero, creep, creep2, creep3, creep4};
        Unit.moveUnit(hero, 16, 16);
        Unit.moveUnit(creep, 16, 16);
        Unit.moveUnit(creep2, 16, 16);
        Unit.moveUnit(creep3, 16, 16);
        Unit.moveUnit(creep4, 16, 16);
        t4.attack(units);
        t4.turnOnGlyph();
        creep.turnOnGlyph();
        t4.attack(units);
        creep.turnOffGlyph();
        t4.attack(units);
        t4.attack(units);
        System.out.println(hero.getHealthPoint() + " " + creep.getHealthPoint()
                           + " " + creep2.getHealthPoint() + " " + creep3.getHealthPoint()
                           + " " + creep4.getHealthPoint());
    }
}

interface Glyph {
    void turnOnGlyph();

    void turnOffGlyph();

    boolean isGlyphActive();
}

class Tower implements Glyph {
    private int damage;
    private int armor;
    private int xCoordinate;
    private int yCoordinate;
    private final int RADIUS = 3;
    protected boolean glyphActive;

    public int getDamage() {
        return damage;
    }

    public int getArmor() {
        return armor;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    @Override
    public boolean isGlyphActive() {
        return glyphActive;
    }

    @Override
    public void turnOnGlyph() {
        glyphActive = true;
    }

    @Override
    public void turnOffGlyph() {
        glyphActive = false;
    }


    public Tower(int damage, int armor, int xCoordinate, int yCoordinate) {
        this.damage = damage;
        this.armor = armor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public boolean inRadius(Unit target) {
        return Math.sqrt(Math.pow(xCoordinate - target.getxCoordinate(), 2)
                         + Math.pow(yCoordinate - target.getyCoordinate(), 2)) <= RADIUS;
    }

    public void attack(Unit[] units) {
        for (Unit unit : units) {
            if (!unit.isDead() && inRadius(unit) && unit instanceof Creep) {
                unit.takeDamage(damage);
                return;
            }
        }
        for (Unit unit : units) {
            if (inRadius(unit)) {
                unit.takeDamage(damage);
                return;
            }
        }
    }

}

class EnhancedTower extends Tower implements Glyph {
    public EnhancedTower(int damage, int armor, int xCoordinate, int yCoordinate) {
        super(damage, armor, xCoordinate, yCoordinate);

    }


    @Override
    public void attack(Unit[] units) {
        int hits = 0;
        if (isGlyphActive()) {
            for (Unit unit : units) {
                if (!unit.isDead() && inRadius(unit)) {
                    unit.takeDamage(getDamage());
                    hits++;
                    if (hits == 3) {
                        break;
                    }
                }
            }
        } else {
            for (Unit unit : units) {
                if (!unit.isDead() && inRadius(unit) && unit instanceof Creep) {
                    unit.takeDamage(getDamage());
                    return;
                }
            }
            for (Unit unit : units) {
                if (inRadius(unit)) {
                    unit.takeDamage(getDamage());
                    return;
                }
            }
        }
    }
}


abstract class Unit implements Glyph {
    private int healthPoint;
    private int xCoordinate;
    private int yCoordinate;
    protected boolean glyphActive;
    private Direction direction;

    public Unit(int healthPoint, int xCoordinate, int yCoordinate, Direction direction) {
        this.healthPoint = healthPoint;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
    }

    @Override
    public boolean isGlyphActive() {
        return glyphActive;
    }

    @Override
    public void turnOnGlyph() {
        glyphActive = true;
    }

    @Override
    public void turnOffGlyph() {
        glyphActive = false;
    }

    public boolean isDead() {
        return getHealthPoint() <= 0;
    }

    public void takeDamage(int damage) {
        this.healthPoint -= damage;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Direction getDirection() {
        return direction;
    }

    public void turnRight() {
        if (direction == Direction.LEFT) {
            direction = Direction.UP;
        } else if (direction == Direction.UP) {
            direction = Direction.RIGHT;
        } else if (direction == Direction.RIGHT) {
            direction = Direction.DOWN;
        } else if (direction == Direction.DOWN) {
            direction = Direction.LEFT;
        }
    }

    public void stepForward() {
        if (direction == Direction.LEFT) {
            xCoordinate--;
        }
        if (direction == Direction.DOWN) {
            yCoordinate--;
        }
        if (direction == Direction.UP) {
            yCoordinate++;
        }
        if (direction == Direction.RIGHT) {
            xCoordinate++;
        }
    }


    public static void unitDirection(Unit unit, Direction direction) {
        while (unit.getDirection() != direction) {
            unit.turnRight();

        }
    }

    public static void moveForward(Unit unit, int steps) {
        while (steps > 0) {
            unit.stepForward();
            steps--;

        }
    }

    public static void moveUnit(Unit unit, int toX, int toY) {
        if (unit.getxCoordinate() < toX) {
            unitDirection(unit, Unit.Direction.RIGHT);
            moveForward(unit, toX - unit.getxCoordinate());
        } else if (unit.getxCoordinate() > toX) {
            unitDirection(unit, Unit.Direction.LEFT);
            moveForward(unit, unit.getxCoordinate() - toX);
        }
        if (unit.getyCoordinate() < toY) {
            unitDirection(unit, Unit.Direction.UP);
            moveForward(unit, toY - unit.getyCoordinate());
        } else if (unit.getyCoordinate() > toY) {
            unitDirection(unit, Unit.Direction.DOWN);
            moveForward(unit, unit.getyCoordinate() - toY);
        }
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}

class Creep extends Unit implements Glyph {
    public Creep(int healthPoint, int xCoordinate, int yCoordinate) {
        super(healthPoint, xCoordinate, yCoordinate, Direction.RIGHT);
    }

    @Override
    public void takeDamage(int damage) {
        if (!isGlyphActive()) {
            setHealthPoint(Math.max(0, getHealthPoint() - damage));
        }
    }

}

class Hero extends Unit {
    public Hero(int healthPoint, int xCoordinate, int yCoordinate) {
        super(healthPoint, xCoordinate, yCoordinate, Direction.RIGHT);
    }

    public void respawnInTavern() {
        setxCoordinate(0);
        setyCoordinate(0);
    }

    @Override
    public void takeDamage(int damage) {
        setHealthPoint(Math.max(0, getHealthPoint() - damage));
        if (isDead()) {
            respawnInTavern();
        }
    }
}


