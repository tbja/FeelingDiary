package tbja.co.kr.feelingdiary.box2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import io.realm.Realm;
import io.realm.RealmResults;
import tbja.co.kr.feelingdiary.R;
import tbja.co.kr.feelingdiary.box2d.model.Ball;
import tbja.co.kr.feelingdiary.box2d.model.BallPoint;
import tbja.co.kr.feelingdiary.box2d.model.Diary;
import tbja.co.kr.feelingdiary.box2d.model.FeelName;
import tbja.co.kr.feelingdiary.realmodel.DiaryRealm;
import tbja.co.kr.feelingdiary.realmodel.SelectedBallRealm;
import tbja.co.kr.feelingdiary.utils.Util;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by HAYDN on 1/7/2015.
 */
public class Game {
    public interface BallClickListener {
        void onClickBall(Ball ball);
    }

    public interface BigBallClickListener {
        void onClickBigBall(Ball ball);
    }

    BallClickListener ballClickListener = null;
    BigBallClickListener bigBallClickListener = null;

    static float CircleArcPoint(final float radius, final float x)
    {
        float x2 = x * x;
        float r2 = radius * radius;

        float y = (float) Math.sqrt(r2-x2);
        return y;
    }

    private AppCompatActivity Parent = null;
    TextView textView;
    int Width,Height;
    Matrix4f View = new Matrix4f();
    Texture boxTexture,groundTexture,trailTexture,backTexture;
    Sprite boxSprite,groundSprite,trailSprite,backSprite;
    Texture[] bigBallTexture = new Texture[Ball.ballResource.length];
    Sprite[] bigBallSprite = new Sprite[Ball.ballResource.length];
    Texture[] middleBallTexture = new Texture[Ball.ballMiddleResource.length];
    Sprite[] middleBallSprite = new Sprite[Ball.ballMiddleResource.length];
    Texture[] numberTexture = new Texture[31];
    Sprite[] numberSprite = new Sprite[31];
    Texture[] feelingNameTexture = new Texture[FeelName.FEELINGCOUNT];
    Sprite[] feelingNameSprite = new Sprite[FeelName.FEELINGCOUNT];

    BallPoint[] ballPoint = new BallPoint[31];
    BallPoint[] ballPoint2 = new BallPoint[FeelName.FEELINGCOUNT];
    FeelName feelName;

    World b2World;
    Vector4f DrawWhite = new Vector4f(1.0f,1.0f,1.0f,1.0f);

    Semaphore semaphore;

    HashMap<Integer,Diary> diaryHashMap = new HashMap<>();

    ArrayList<Body> removeBodies = new ArrayList<>();
    ArrayList<BodyDef> createBodies = new ArrayList<>();
    ArrayList<FixtureDef> createFixtures = new ArrayList<>();

    int year;
    int month;
    int day;
    int lastDay;

    public boolean updateFlag = false;
    int clickBallId = -1;

    public Game(AppCompatActivity parent, Semaphore semaphore,int y,int m,int d)
    {
        this.semaphore = semaphore;
        Parent = parent;
        //textView = (TextView)Parent.findViewById(R.id.frame_rate);

        b2World = new World(new Vec2(0.0f,-9.81f));

        year = y;
        month = m;
        day = d;

        feelName = new FeelName(Parent.getApplicationContext());

        Realm.init(parent);
        initBallPoint();
        initBallPoint2();
    }

    public void initBallPoint2() {
        ballPoint2[0] = new BallPoint(6.3f,7.3f);
        ballPoint2[1] = new BallPoint(2.7f,10.77f);
        ballPoint2[2] = new BallPoint(10.11f,10.4f);
        ballPoint2[3] = new BallPoint(3.1f,4.0f);
        ballPoint2[4] = new BallPoint(9.4f,3.7f);
    }

    public void initBallPoint() {
        ballPoint[0] = new BallPoint(1.1f,1.0f);
        ballPoint[1] = new BallPoint(3.2f,1.0f);
        ballPoint[2] = new BallPoint(5.2f,1.0f);
        ballPoint[3] = new BallPoint(7.2f,1.0f);
        ballPoint[4] = new BallPoint(9.2f,1.0f);
        ballPoint[5] = new BallPoint(11.2f,1.0f);
        ballPoint[6] = new BallPoint(12.2f,2.9f);
        ballPoint[7] = new BallPoint(10.6f,2.8f);
        ballPoint[8] = new BallPoint(8.3f,2.8f);
        ballPoint[9] = new BallPoint(6.1f,2.8f);
        ballPoint[10] = new BallPoint(3.9f,2.8f);
        ballPoint[11] = new BallPoint(1.9f,2.8f);
        ballPoint[12] = new BallPoint(3.2f,4.4f);
        ballPoint[13] = new BallPoint(5.2f,4.4f);
        ballPoint[14] = new BallPoint(7.2f,4.4f);
        ballPoint[15] = new BallPoint(9.2f,4.4f);
        ballPoint[16] = new BallPoint(11.2f,4.4f);
        ballPoint[17] = new BallPoint(12.2f,6.2f);
        ballPoint[18] = new BallPoint(10.6f,6.2f);
        ballPoint[19] = new BallPoint(8.3f,6.2f);
        ballPoint[20] = new BallPoint(6.1f,6.2f);
        ballPoint[21] = new BallPoint(3.9f,6.2f);
        ballPoint[22] = new BallPoint(1.9f,6.2f);
        ballPoint[23] = new BallPoint(3.2f,8.2f);
        ballPoint[24] = new BallPoint(5.2f,8.2f);
        ballPoint[25] = new BallPoint(7.2f,8.2f);
        ballPoint[26] = new BallPoint(9.2f,8.2f);
        ballPoint[27] = new BallPoint(1.9f,10.2f);
        ballPoint[28] = new BallPoint(3.2f,10.2f);
        ballPoint[29] = new BallPoint(5.2f,10.2f);
        ballPoint[30] = new BallPoint(7.2f,10.2f);

    }

    public void Reset(final boolean newBallLoad)
    {
        removeAllBodies();
        CreateFloor(new Vec2(6.5f, 0f));
        CreateFloor(new Vec2(6.5f, 23.12f));
        CreateWall(new Vec2(0f, 0f));
        CreateWall(new Vec2(13f, 0f));

        loadDiarys(year,month,day,0,0,newBallLoad);

    }

    public void newBallCreate(int year,int month,int day) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<DiaryRealm> todayDiaryRealm = realm.where(DiaryRealm.class).
                equalTo("year", year).equalTo("month", month).equalTo("day", day).findAll();

        Diary diary = Diary.copyFromRealmObj(todayDiaryRealm.first());
        realm.close();

        CreateBall(new Vec2(6.2f, 23.0f), null, diary);
    }

    public void movePrevDiary() {

        destroyAllBall();
        loadDiarys(year,month,day,0,1,false);
    }

    public void moveNextDiary() {
        destroyAllBall();
        loadDiarys(year,month,day,0,1,false);
    }


    public void destroyAllBall() {
        List<Body> bodies = new ArrayList<Body>();
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);
            Ball.ObjectType tmpObj;
            Ball ball = null;
            if (b.getUserData() instanceof Ball) {
                ball = (Ball)b.getUserData();
                tmpObj = ball.getType();

                if (tmpObj == Ball.ObjectType.Ball) {
                    removeBodies.add(b);
                }
            }
        }
    }

    public void loadDiarys(int y,int m,int d,int flag,int yFlag,boolean newFlag) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<DiaryRealm> diaries = realm.where(DiaryRealm.class).
                equalTo("year",y).equalTo("month",m).findAll();

        diaryHashMap.clear();
        for (DiaryRealm item : diaries) {
            Diary diary = Diary.copyFromRealmObj(item);
            diaryHashMap.put(item.getDay(), diary);
        }
        realm.close();

        Calendar c = Calendar.getInstance();
        int todayYear = c.get(Calendar.YEAR);
        int todayMonth = c.get(Calendar.MONTH);
        int todayDay = c.get(Calendar.DAY_OF_MONTH);

        c.set(Calendar.YEAR,y);
        c.set(Calendar.MONTH,m);

        int tmpDay = 0;
        if (todayYear == y && todayMonth == m) {
            tmpDay = todayDay;
        } else {
            tmpDay = c.getActualMaximum(Calendar.DATE);
        }
        c.set(Calendar.DAY_OF_MONTH,tmpDay);

        int tmpFlag = 0;

        for (Integer i = 1 ; i <= tmpDay-flag ; i++) {
            Diary diary = null;
            if (newFlag && i == day) {
                tmpFlag = 1;

            } else {
                if (diaryHashMap.get(i) == null) {
                    diary = new Diary();
                    diary.setBallColor(5);
                    diary.setYear(y);
                    diary.setMonth(m);
                    diary.setDay(i);
                } else {
                    diary = diaryHashMap.get(i);
                }
                CreateBall(new Vec2(ballPoint[i - 1 - tmpFlag].getX(), ballPoint[i - 1 - tmpFlag].getY() + yFlag), null, diary);
            }
        }

        year = y;
        month = m;
        day = tmpDay;
        lastDay = tmpDay;
    }

    public void Reset2()
    {
        removeAllBodies();
        //for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) b2World.destroyBody(b);
        CreateFloor(new Vec2(6.5f, 0f));
        CreateFloor(new Vec2(6.5f, 23.12f));
        CreateWall(new Vec2(0f, 0f));
        CreateWall(new Vec2(13f, 0f));

        loadFeelingBalls();

    }

    public void loadFeelingBalls() {

        ArrayList<Integer> ids = new ArrayList<>();
        float offset = 6.0f;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SelectedBallRealm> selectedBallRealms = realm.where(SelectedBallRealm.class).findAll();

        for (int i = 0 ; i < selectedBallRealms.size() ; i++) {
            ids.add(selectedBallRealms.get(i).getId());
        }

        realm.close();

        for (int i = 0 ; i < ids.size() ; i++) {
            CreateBall2(new Vec2(ballPoint2[i].getX(),ballPoint2[i].getY() + offset-1),null,ids.get(i));
        }
    }

    private void CreateBox(Vec2 position)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = new Vec2(0.0f,0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = (Object) Ball.ObjectType.Box;
        bodyDef.type = BodyType.DYNAMIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.35f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 0.75f;
        fixtureDef.isSensor = false;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }


    private void CreateBall(Vec2 position, @Nullable Vec2 velocity,Diary diary)
    {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        Ball ball = new Ball(diary.getBallColor(), Ball.ObjectType.Ball);
        ball.setDiary(diary);

        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = v;
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = false;
        bodyDef.gravityScale =2.5f;
        bodyDef.linearDamping = 1f;
        bodyDef.angularDamping = 1f;
        bodyDef.userData = ball;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.45f;
        fixtureDef.restitution = 0.75f;
        fixtureDef.density = 25.0f;
        fixtureDef.isSensor = false;

        createBodies.add(bodyDef);
        createFixtures.add(fixtureDef);
    }

    public void removeBall(Diary removeDiary) {
        List<Body> bodies = new ArrayList<Body>();
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);

            Ball ball = null;
            Ball.ObjectType tmpObj = null;

            if (b.getUserData() instanceof Ball) {
                ball = (Ball)b.getUserData();
                tmpObj = ball.getType();
            } else {
                tmpObj = (Ball.ObjectType)b.getUserData();
            }

            if (tmpObj == Ball.ObjectType.Ball)
            {
                if (ball.getDiary().getDay() == removeDiary.getDay()) {
                    Diary diary = new Diary();
                    diary.setId(0);
                    diary.setBallColor(5);
                    diary.setYear(removeDiary.getYear());
                    diary.setMonth(removeDiary.getMonth());
                    diary.setDay(removeDiary.getDay());
                    ball.setDiary(diary);
                    b.setUserData(ball);
                    break;
                }
            }
        }
    }

    public void removeAllBodies() {
        List<Body> bodies = new ArrayList<Body>();
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);
            removeBodies.add(b);
        }
    }

    public void removeAllFeelingBalls() {
        List<Body> bodies = new ArrayList<Body>();
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);

            Ball ball = null;
            Ball.ObjectType tmpObj = null;

            if (b.getUserData() instanceof Ball) {
                ball = (Ball)b.getUserData();
                tmpObj = ball.getType();
            } else {
                tmpObj = (Ball.ObjectType)b.getUserData();
            }

            if (tmpObj == Ball.ObjectType.Ball2)
            {
                removeBodies.add(b);
            }
        }
    }

    public void modifyBall(Diary modifyDiary) {
        List<Body> bodies = new ArrayList<Body>();
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
        for (int i = bodies.size() - 1; i >= 0; i--) {
            Body b = bodies.get(i);

            Ball ball = null;
            Ball.ObjectType tmpObj = null;

            if (b.getUserData() instanceof Ball) {
                ball = (Ball)b.getUserData();
                tmpObj = ball.getType();
            } else {
                tmpObj = (Ball.ObjectType)b.getUserData();
            }

            if (tmpObj == Ball.ObjectType.Ball)
            {
                if (ball.getDiary().getDay() == modifyDiary.getDay()) {
                    ball.setDiary(modifyDiary);
                    b.setUserData(ball);
                }
            }
        }
    }

    private void CreateBall2(Vec2 position, @Nullable Vec2 velocity,int ballColor)
    {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        Ball ball = new Ball(ballColor, Ball.ObjectType.Ball2);


        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = v;
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = false;
        bodyDef.gravityScale =2.5f;
        bodyDef.linearDamping = 1f;
        bodyDef.angularDamping = 1f;
        bodyDef.userData = ball;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(2.573f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.45f;
        fixtureDef.restitution = 0.75f;
        fixtureDef.density = 25.0f;
        fixtureDef.isSensor = false;

        createBodies.add(bodyDef);
        createFixtures.add(fixtureDef);
    }

    private void CreateWall(Vec2 position)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = new Vec2(0.0f,0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = (Object) Ball.ObjectType.Floor;
        bodyDef.type = BodyType.KINEMATIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.05f,23.11f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = false;

        //Body body = b2World.createBody(bodyDef);
        //body.createFixture(fixtureDef);
        createBodies.add(bodyDef);
        createFixtures.add(fixtureDef);
    }

    private void CreateFloor(Vec2 position)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.angle = 0.0f;
        bodyDef.linearVelocity = new Vec2(0.0f,0.0f);
        bodyDef.angularVelocity = 0.0f;
        bodyDef.fixedRotation = false;
        bodyDef.active = true;
        bodyDef.bullet = false;
        bodyDef.allowSleep = true;
        bodyDef.gravityScale = 1.0f;
        bodyDef.linearDamping = 0.0f;
        bodyDef.angularDamping = 0.0f;
        bodyDef.userData = (Object) Ball.ObjectType.Floor;
        bodyDef.type = BodyType.KINEMATIC;

        ChainShape cShape = new ChainShape();
        //cShape.createChain();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6.5f,0.05f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = false;

        //Body body = b2World.createBody(bodyDef);
        //body.createFixture(fixtureDef);

        createBodies.add(bodyDef);
        createFixtures.add(fixtureDef);
    }

    public void Init(int back_res) {


        Square.InitSquare();

        // Enable 2D Textures
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // Enable Culling
        //GLES20.glFrontFace(GLES20.GL_CCW);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glCullFace(GLES20.GL_BACK);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        backTexture = new Texture(Parent.getApplicationContext(),back_res);
        backSprite = new Sprite(backTexture);

        boxTexture = new Texture(Parent.getApplicationContext(), R.drawable.box);
        boxSprite = new Sprite(boxTexture);

        for (int i = 0 ; i < middleBallTexture.length ; i++) {
            middleBallTexture[i] = new Texture(Parent.getApplicationContext(), Ball.ballMiddleResource[i]);
            middleBallSprite[i] = new Sprite(middleBallTexture[i]);
        }

        for (int i = 0 ; i < bigBallTexture.length ; i++) {
            bigBallTexture[i] = new Texture(Parent.getApplicationContext(), Ball.ballResource[i]);
            bigBallSprite[i] = new Sprite(bigBallTexture[i]);

        }

        for (int i = 0 ; i < numberTexture.length ; i++) {
            numberTexture[i] = new Texture(Parent.getApplicationContext(),createNumberBitmap((i+1) +""));
            numberSprite[i] = new Sprite(numberTexture[i]);
        }

        for (int i = 0 ; i < FeelName.FEELINGCOUNT ; i++) {
            feelingNameTexture[i] = new Texture(Parent.getApplicationContext(),createFeelingNameBitmap(feelName.getFeelingOneName(i)));
            feelingNameSprite[i] = new Sprite(feelingNameTexture[i]);
        }

        trailTexture = new Texture(Parent.getApplicationContext(), R.drawable.trail);
        trailSprite = new Sprite(trailTexture);

        groundTexture = new Texture(Parent.getApplicationContext(), R.drawable.ground);
        groundSprite = new Sprite(groundTexture);
        GLES20.glClearColor(235f / 255.0f, 235f / 255.0f, 255f / 255.0f, 255f / 255.0f);

    }

    public Bitmap createFeelingNameBitmap(String feelingName) {
        Bitmap bitmap = Bitmap.createBitmap(599,599,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Paint pnt = new Paint();
        pnt.setAntiAlias(true);
        pnt.setColor(Color.parseColor("#ffffff"));
        pnt.setTextSize(80);
        pnt.setTextAlign(Paint.Align.CENTER);
        pnt.setShadowLayer(5.0f,5.0f,5.0f,Color.parseColor("#4C000000"));
        canvas.drawText(feelingName,599/2,599/2+80/2,pnt);


        return bitmap;
    }

    public Bitmap createNumberBitmap(String number) {
        Bitmap bitmap = Bitmap.createBitmap(250,250,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Paint pnt = new Paint();
        pnt.setAntiAlias(true);
        pnt.setColor(Color.parseColor("#B2ffffff"));
        pnt.setTextSize(50);
        pnt.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number,250/2,250/2+50/2,pnt);


        return bitmap;
    }

    public void Update(float delta)
    {
//        if(IsSlow) b2World.step(delta * 0.2f, 20, 20);
//        else b2World.step(delta, 20, 20);
        b2World.step(delta, 20, 20);

        for (Body b : removeBodies) {
            b2World.destroyBody(b);
        }
        removeBodies.clear();

        for (int i = 0 ; i < createFixtures.size() ; i++) {
            Body body = b2World.createBody(createBodies.get(i));
            body.createFixture(createFixtures.get(i));
        }

        createFixtures.clear();
        createBodies.clear();
    }

    public void Draw(float ax, float ay,float az)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        b2World.setGravity(new Vec2(-ax, -ay));

        backSprite.Draw(new Vec2(6.5f,11.56f), 0, 13f / 1440, View);

        int num_objects = b2World.getBodyCount();
        if(num_objects >= 0)
        {
            Body body = b2World.getBodyList();
            for(int i=0;i<num_objects;i++)
            {
                Square.Color = DrawWhite;
                Ball.ObjectType tmpObj;
                Ball ball = null;
                if (body.getUserData() instanceof Ball) {
                    ball = (Ball)body.getUserData();
                    tmpObj = ball.getType();
                } else {
                    tmpObj = (Ball.ObjectType)body.getUserData();
                }

                switch(tmpObj)
                {
                    case Box:
                        boxSprite.Draw(body.getWorldCenter(), body.getAngle(), 1.05f / 94.0f, View);
                        break;
                    case Floor:
                        groundSprite.Draw(body.getWorldCenter(), body.getAngle(), 1.05f / 100.0f, View);
                        break;
                    case Ball:
                        Vec2 position = body.getWorldCenter();
                        int tmpBallColor = 5;

                        if (ball.getDiary() != null) {
                            tmpBallColor = ball.getDiary().getBallColor();
                        }
                        middleBallSprite[tmpBallColor].Draw(position, body.getAngle(), ((1.05f / 100.0f)*0.82f), View);
                        numberSprite[ball.getDiary().getDay()-1].Draw(position, body.getAngle(), 9.15f / 1000.0f,View);

                        Vec2 velocity = new Vec2(body.getLinearVelocity());
                        float length = velocity.length();
                        velocity.normalize();
                        if(length > 1.0)
                        {
                            Vec2 direction = new Vec2(-velocity.y,velocity.x);

                            final float trail_offset_x = 0.3f;
                            final float trail_offset_l = 0.1f;
                            final float base_size = 1.05f / 48.0f;
                            final float length_size = base_size * length * 3.5f;

                            Vec2 offsetA = new Vec2(direction.x * trail_offset_x,direction.y * trail_offset_x); // Left
                            Vec2 offsetB = new Vec2(direction.x * -trail_offset_x,direction.y * -trail_offset_x); // Right


                            final float side_distance = -(CircleArcPoint(0.5f+trail_offset_l/2.0f,trail_offset_x) + trail_offset_l + length_size/2);
                            final float middle_distance = -(CircleArcPoint(0.5f+trail_offset_l/2.0f,0.0f) + trail_offset_l + length_size/2);
                            Vec2 offsetC = new Vec2(velocity.x * side_distance, velocity.y * side_distance); // Backwards Offset Sides
                            Vec2 offsetD = new Vec2(velocity.x * middle_distance, velocity.y * middle_distance); // Backwards Offset Middle


                            Vec2 positionA = new Vec2(position.x + offsetC.x + offsetA.x, position.y + offsetC.y + offsetA.y);
                            Vec2 positionB = new Vec2(position.x + offsetC.x + offsetB.x, position.y + offsetC.y + offsetB.y);
                            Vec2 positionC = new Vec2(position.x + offsetD.x, position.y + offsetD.y);
                            //Vec2 positionB = new Vec2(offsetB.x + offsetC.x + position.x,offsetB.y+offsetC.y + position.y);
                            //Vec2 positionC = new Vec2(velocity.x * middle_distance + position.x,velocity.y * middle_distance + position.y);


                            float angle = (float) Math.atan2(direction.y,-direction.x);
                            float transparency = Math.min((length - 1.0f) / 5.0f,1.0f);
                            transparency *= transparency;
                            Square.Color = new Vector4f(1.0f,1.0f,1.0f,transparency);
                            //trailSprite.Draw(positionA, angle, new Vec2(base_size,length_size), View);
                            //trailSprite.Draw(positionB, angle, new Vec2(base_size,length_size), View);
                            //trailSprite.Draw(positionC, angle, new Vec2(base_size,length_size), View);
                        }


                        break;
                    case Ball2:
                        Vec2 position2 = body.getWorldCenter();

                        bigBallSprite[ball.getBallColor()].Draw(position2, body.getAngle(), 9.15f / 1000.0f, View);
                        feelingNameSprite[ball.getBallColor()].Draw(position2, body.getAngle(), 9.15f / 1000.0f, View);


                        Vec2 velocity2 = new Vec2(body.getLinearVelocity());
                        float length2 = velocity2.length();
                        velocity2.normalize();
                        if(length2 > 1.0)
                        {
                            float transparency = Math.min((length2 - 1.0f) / 5.0f,1.0f);
                            transparency *= transparency;
                            Square.Color = new Vector4f(1.0f,1.0f,1.0f,transparency);
                        }


                        break;
                }
                body = body.getNext();
            }
        }
    }


    public void UI()
    {

    }

    public void TouchEvent(MotionEvent e)
    {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case ACTION_UP:
                int num_bodies = b2World.getBodyCount();
                float mouseX = (13.0f * e.getX() / Width);
                float mouseY = (13.0f * ((float) Height / (float) Width) * (1.0f - e.getY() / Height));
                //Log.d("aaa","mouse : " + mouseX + "==" + mouseY);

                List<Body> bodies = new ArrayList<Body>();
                for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
                for (int i = bodies.size() - 1; i >= 0; i--) {
                    Body b = bodies.get(i);

                    Ball ball = null;
                    Ball.ObjectType tmpObj = null;

                    if (b.getUserData() instanceof Ball) {
                        ball = (Ball)b.getUserData();
                        tmpObj = ball.getType();
                    } else {
                        tmpObj = (Ball.ObjectType)b.getUserData();
                    }

                    if (tmpObj == Ball.ObjectType.Ball)
                    {
                        float ball_width_2 = 1.194f;
                        float ball_height_2 = 1.194f;
                        //Log.d("aaa","i : " + i);
                        if (mouseX >= (b.getWorldCenter().x-ball_width_2) && mouseX <= (b.getWorldCenter().x+ball_width_2)) {
                            if (mouseY >= (b.getWorldCenter().y-ball_height_2) && mouseY <= (b.getWorldCenter().y+ball_height_2)) {
                                if (e.getAction() == ACTION_UP) {
                                    if (clickBallId == ball.getDiary().getDay()) {
                                        if (ballClickListener != null) {
                                            ballClickListener.onClickBall(ball);
                                        }
                                        clickBallId=-1;
                                    }
                                } else {
                                    clickBallId = ball.getDiary().getDay();
                                }

                                break;
                            }
                        }

                    } else if (tmpObj == Ball.ObjectType.Ball2) {
                        if (e.getAction() == ACTION_UP) {
                            float ball_width_2 = 2.9f;
                            float ball_height_2 = 2.9f;
                            if (mouseX >= (b.getWorldCenter().x - ball_width_2) && mouseX <= (b.getWorldCenter().x + ball_width_2)) {
                                if (mouseY >= (b.getWorldCenter().y - ball_height_2) && mouseY <= (b.getWorldCenter().y + ball_height_2)) {
                                    if (bigBallClickListener != null) {
                                        bigBallClickListener.onClickBigBall(ball);
                                    }

                                    break;
                                }
                            }
                        }
                    }

                }
                break;
        }


        /*
        if(num_bodies > 50) {
            List<Body> bodies = new ArrayList<Body>();
            for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) bodies.add(b);
            for (int i = bodies.size() - 1; i >= 0; i--) {
                Body b = bodies.get(i);
                if ((ObjectType) b.getUserData() != ObjectType.Floor) {
                    b2World.destroyBody(b);
                    break;
                }
            }
        }
        if(e.getAction() == MotionEvent.ACTION_DOWN)
        {
            switch(SpawnType)
            {
                case Box:
                    CreateBox(new Vec2(13.0f * e.getX() / Width, 13.0f * ((float) Height / (float) Width) * (1.0f - e.getY() / Height)));
                    break;
                case Ball:
                    CreateBall(new Vec2(13.0f * e.getX() / Width, 13.0f * ((float) Height / (float) Width) * (1.0f - e.getY() / Height)), null);
                    break;
            }
        }
        */
    }

    public void SetSize(int width, int height) {
        Width = width;
        Height = height;

        final float height_ratio = ((float)height)/((float)width);
        final float base_units = 13f;
        final float pixels_per_unit = 100.0f;
        float virtual_width = base_units;
        float virtual_height = virtual_width * height_ratio;

        View = new Matrix4f().ortho(0,virtual_width,0,virtual_height,1,-1);
        //.orthoM(View,0,0,Width,0,Height,1,-1);
    }

    public BallClickListener getBallClickListener() {
        return ballClickListener;
    }

    public void setBallClickListener(BallClickListener ballClickListener) {
        this.ballClickListener = ballClickListener;
    }

    public void setBigBallClickListener(BigBallClickListener bigBallClickListener) {
        this.bigBallClickListener = bigBallClickListener;
    }

    public void onDestroy() {
        b2World = null;
    }

    public World getB2World() {
        return b2World;
    }

    public void setDateVar(int y,int m,int d) {
        year = y;
        month = m;
        day = d;
    }

    public int getLastDay() {
        return lastDay;
    }
}
