public class Dota {
    public static void main(String[] args) {
        Unit hero = new Hero(1500, 10, 10);
        Unit creep = new Creep(500, 8, 8, false);
        Unit creep2 = new Creep(500, 8, 8, false);
        Unit creep3 = new Creep(500, 8, 8, false);
        Unit creep4 = new Creep(500, 8, 8, false);
        Tower t3 = new EnhancedTower(170, 16, 15, 15, true);
        Tower t4 = new EnhancedTower(174, 21, 15, 15, true);
        Tower t1 = new Tower(100, 10, 15, 15);
        Unit[] units = {hero, creep, creep2, creep3, creep4};
        Unit.moveUnit(hero, 16, 16);
        Unit.moveUnit(creep, 16, 16);
        Unit.moveUnit(creep2, 16, 16);
        Unit.moveUnit(creep3, 16, 16);
        Unit.moveUnit(creep4, 16, 16);
        t1.attack(units);
        System.out.println(hero.getHealthPoint() + " " + creep.getHealthPoint() + " " + creep2.getHealthPoint() + " " + creep3.getHealthPoint() + " " + creep4.getHealthPoint());
    }
}

class Tower {
    protected int damage;
    protected int armor;
    protected int xCoordinate;
    protected int yCoordinate;
    private final int RADIUS = 3;
    protected boolean isGlyphActive;
    protected int hits;

    public Tower(int damage, int armor, int xCoordinate, int yCoordinate) {
        this.damage = damage;
        this.armor = armor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public boolean inRadius(Unit target) {
        return xCoordinate - RADIUS <= target.getxCoordinate() && target.getxCoordinate() <= xCoordinate + RADIUS &&
                yCoordinate - RADIUS <= target.getyCoordinate() && target.getyCoordinate() <= yCoordinate + RADIUS;
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


    public int getRADIUS() {
        return RADIUS;
    }
}

class EnhancedTower extends Tower {
    public EnhancedTower(int damage, int armor, int xCoordinate, int yCoordinate, boolean isGlyphActive) {
        super(damage, armor, xCoordinate, yCoordinate);
        this.isGlyphActive = isGlyphActive;
    }

    @Override
    public void attack(Unit[] units) {
        if (isGlyphActive) {
            for (Unit unit : units) {
                if (inRadius(unit)) {
                    unit.takeDamage(damage);
                    hits++;
                    if (hits == 3) {
                        break;
                    }
                }
            }
        } else {
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
}


abstract class Unit {
    private int healthPoint;
    private int xCoordinate;
    private int yCoordinate;
    protected boolean isGlyphActive;
    private Direction direction;

    public Unit(int healthPoint, int xCoordinate, int yCoordinate, Direction direction) {
        this.healthPoint = healthPoint;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
    }

    public boolean isDead() {
        return getHealthPoint() == 0;
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
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}

class Creep extends Unit {
    public Creep(int healthPoint, int xCoordinate, int yCoordinate, boolean isGlyphActive) {
        super(healthPoint, xCoordinate, yCoordinate, Direction.RIGHT);
        this.isGlyphActive = isGlyphActive;
    }

    @Override
    public void takeDamage(int damage) {
        if (isGlyphActive) {
            getHealthPoint();
        } else {
            setHealthPoint(Math.max(0, getHealthPoint() - damage));
        }
    }

}

class Hero extends Unit {
    public Hero(int healthPoint, int xCoordinate, int yCoordinate) {
        super(healthPoint, xCoordinate, yCoordinate, Direction.RIGHT);
    }

    @Override
    public void takeDamage(int damage) {
        setHealthPoint(Math.max(0, getHealthPoint() - damage));
        if (isDead()) {
            setxCoordinate(0);
            setyCoordinate(0);
        }
    }
}





