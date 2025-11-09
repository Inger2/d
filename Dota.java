package oop;

public class Dota {
    public static void main(String[] args) {
        Unit hero = new Hero(1500, 10, 10);
        Unit creep = new Creep(500, 8, 8,false);
        Unit creep2 = new Creep(500, 8, 8,false);
        Unit creep3 = new Creep(500, 8, 8,false);
        Unit creep4 = new Creep(500, 8, 8,false);
        Tower t3 = new Tier3Tower(false);
        Tower t4 = new Tier3Tower(false);

        Unit[] units={hero,creep,creep2,creep3,creep4};
        Unit.moveUnit(hero, 16, 16);
        Unit.moveUnit(creep, 16, 16);
        Unit.moveUnit(creep2, 16, 16);
        Unit.moveUnit(creep3, 16, 16);
        Unit.moveUnit(creep4, 16, 16);
        t3.attack(units);
        t3.attack(units);
        t3.attack(units);
        t3.attack(units);
        t3.attack(units);
        System.out.println(hero.getHP()+ " "+ creep.getHP()+" " +creep2.getHP()+" " +creep3.getHP()+" " +creep4.getHP());


    }
}

abstract class Tower {
    protected final int DAMAGE;
    private final int ARMOR;
    private final int xCoordinate;
    private final int yCoordinate;
    private final int RADIUS = 3;
    protected boolean isGlyphActive;
    protected int hits;

    public Tower(int DAMAGE, int ARMOR, int xCoordinate, int yCoordinate) {
        this.DAMAGE = DAMAGE;
        this.ARMOR = ARMOR;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public boolean inRadius(Unit target) {
        return xCoordinate - RADIUS <= target.getxCooordinate() && target.getxCooordinate() <= xCoordinate + RADIUS && //тут не слишком ли сложное условие?
                yCoordinate - RADIUS <= target.getyCoordinate() && target.getyCoordinate() <= yCoordinate + RADIUS;
    }

    public void attack(Unit[] units) {
    }



    public int getRADIUS() {
        return RADIUS;
    }
}

class Tier3Tower extends Tower {
    public Tier3Tower(boolean isGlyphActive) {
        super(170, 16, 15, 15);
        this.isGlyphActive = isGlyphActive;
    }

    @Override
    public void attack(Unit[] units) {
        if (isGlyphActive) {
            for (Unit unit : units) {
                if (inRadius(unit)) {
                    {
                        unit.takeDamage(DAMAGE);
                        hits++;
                        if (hits == 3) {
                            break;
                        }
                    }
                }
            }
        } else {
            for (Unit unit : units) {
                if (!unit.isDead() && inRadius(unit) && unit instanceof Creep) { // тут не работает
                    unit.takeDamage(DAMAGE);
                    return;
                }
            }
            for (Unit unit : units) {
                if (inRadius(unit) && unit instanceof Hero) {
                    unit.takeDamage(DAMAGE);
                    return;
                }
            }
        }
    }
}

class Tier4Tower extends Tower {
    public Tier4Tower(boolean isGlyphActive) {
        super(174, 21, 25, 25);
        this.isGlyphActive = isGlyphActive;
    }
    @Override
    public void attack(Unit[] units) {
        if (isGlyphActive) {
            for (Unit unit : units) {
                if (inRadius(unit)) {
                    {
                        unit.takeDamage(DAMAGE);
                        hits++;
                        if (hits == 3) {
                            break;
                        }
                    }
                }
            }
        } else {
            for (Unit unit : units) {
                if (!unit.isDead() && inRadius(unit) && unit instanceof Creep) { // тут не работает
                    unit.takeDamage(DAMAGE);
                    return;
                }
            }
            for (Unit unit : units) {
                if (inRadius(unit) && unit instanceof Hero) {
                    unit.takeDamage(DAMAGE);
                    return;
                }
            }
        }
    }
}

abstract class Unit {
    private int HP;
    private int xCoordinate;
    private int yCoordinate;
    protected boolean isActive;
    private Direction direction;

    public Unit(int HP, int xCoordinate, int yCoordinate, Direction direction) {
        this.HP = HP;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
    }
    public boolean isDead() {
        return getHP() == 0;
    }

    public void takeDamage(int damage) {
        this.HP -= damage;
    }

    public int getHP() {
        return HP;
    }

    public int getxCooordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setxCooordinate(int xCoordinate) {
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
        if (unit.getxCooordinate() < toX) {
            unitDirection(unit, Unit.Direction.RIGHT);
            moveForward(unit, toX - unit.getxCooordinate());
        } else if (unit.getxCooordinate() > toX) {
            unitDirection(unit, Unit.Direction.LEFT);
            moveForward(unit, unit.getxCooordinate() - toX);
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
    public Creep(int HP, int xCoordinate, int yCoordinate,Boolean isActive ) {
        super(HP,xCoordinate ,yCoordinate, Direction.RIGHT);
        this.isActive = isActive;
    }


    public void takeDamage(int damage) {
        if (isActive) {
            getHP();
        } else {
            setHP(Math.max(0, getHP() - damage));
        }
    }


}

class Hero extends Unit {
    public Hero(int HP, int xCoordinate, int yCoordinate) {
        super(HP, xCoordinate, yCoordinate, Direction.RIGHT);
    }



    public void takeDamage(int damage) {
        setHP(Math.max(0, getHP() - damage));
        if (isDead()) {
            setxCooordinate(0);
            setyCoordinate(0);
        }
    }

}
// while(unitxcoord!=toX||unitycoord!=){}


