package edu.neu.madcourse.manusaxena.Assignment8.wordgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import edu.neu.madcourse.manusaxena.Assignment5.MainActivity;
import edu.neu.madcourse.manusaxena.R;

public class WordGameFragment extends Fragment {
    static private int mLargeIds[] = {R.id.large1_word_game, R.id.large2_word_game, R.id.large3_word_game,
         R.id.large4_word_game, R.id.large5_word_game, R.id.large6_word_game, R.id.large7_word_game, R.id.large8_word_game,
         R.id.large9_word_game,};
    static private int mSmallIds[] = {R.id.small1_word_game, R.id.small2_word_game, R.id.small3_word_game,
         R.id.small4_word_game, R.id.small5_word_game, R.id.small6_word_game, R.id.small7_word_game, R.id.small8_word_game,
         R.id.small9_word_game,};
    private static final int[] ColorAvailable = {Color.CYAN,Color.MAGENTA,Color.BLACK};
    private static final int ColorSelected = Color.YELLOW;
    private static final int ColorLocked = Color.BLUE;
    private static final int ColorUnavailable = Color.GRAY;
    private static final int ColorHide = Color.RED;
    public   Integer[][] alphabets;
    public Integer[][] seed={{0,1,2,5,4,3,6,7,8},
                            {1,2,4,5,8,7,6,3,0},
                           {4,2,5,8,7,6,3,0,1},
                           {4,7,6,3,0,1,2,5,8},
                           {0,4,6,3,1,2,5,7,8},
                           {3,4,0,1,2,5,8,7,6},
                           {8,7,6,3,4,0,1,2,5},
                           {2,4,1,0,3,6,7,5,8},
                           {5,2,1,4,8,7,6,3,0},
                           {7,6,4,8,5,2,1,3,0},
                       {7,4,8,5,2,1,0,3,6}};
    public int[] playBoard = {0,1,0,1,2,1,0,1,0};
    public ArrayList<Integer[]> nineLetterWords = new ArrayList<Integer[]>();
    public ArrayList<Long> wordsDetected = new ArrayList<Long>();
    public ArrayList<String> words = new ArrayList<String>();
    private WordGameTile mEntireBoard = new WordGameTile(this);
    private WordGameTile mLargeTiles[] = new WordGameTile[9];
    private WordGameTile mSmallTiles[][] = new WordGameTile[9][9];
    private Set<WordGameTile> mAvailable = new HashSet<WordGameTile>();
    private Stack<WordGameTile> mCurrent = new Stack<WordGameTile>();
    private int mSoundX, mSoundMiss, mSoundRewind;
    private SoundPool mSoundPool;
    private float mVolume = 1f;
    private int mLastLarge;
    private int mLastSmall;
    public long[] wordValues=new long[432335];
    public char[] letters={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private int timer=25;
    private int score1=0,score2=0;
    private TextView t,ts,p1,p2;
    private ArrayAdapter<String> adapter;
    public Handler mHandler = new Handler();
    public Runnable runnable;
    public  int me;
    private static int currentPlayer=1;
    private  String gameId="some game Id in Word Game Fragment";
    private  String ctplayer1="";
    private String ctplayer2="";
    private  String player1="player1";
    private String player2="player2";
    public AlertDialog mDialog;
    private static String clientToken= FirebaseInstanceId.getInstance().getToken();
    private final static DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();


    public void setCtplayer2(String ctplayer2) {
        this.ctplayer2 = ctplayer2;
    }


    public void setCtplayer1(String ctplayer1) {
        this.ctplayer1 = ctplayer1;
    }

    public void setMe(int me)
    {
        this.me = me;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;

    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
        p1.setText(this.player1+":"+score1);

    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
        p2.setText(this.player2+":"+score2);
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        loadDictionary();
        timer=25;
        score1=0;
        score2=0;


        initGame();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, words);
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.wg3, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.wg4, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.wg2, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Waiting for other Player");
        builder.setCancelable(false);
        builder.setPositiveButton("Play Offline",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent homeIntent=new Intent(getActivity().getApplicationContext(), MainActivity.class);

                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mDatabase.child("users/"+clientToken+"/active").setValue(false);
                        startActivity(homeIntent);
                    }
                });
        mDialog = builder.create();

        runnable = new Runnable() {
            public void run() {
                startTimer();
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_tpwg_board, container, false);
        initViews(rootView);
        ListView listView = (ListView) rootView.findViewById(R.id.wordGamelistView);
        listView.setAdapter(adapter);
        t = (TextView)(rootView.findViewById(R.id.timer));
        ts = (TextView)(rootView.findViewById(R.id.topscore));
        p1 = (TextView)(rootView.findViewById(R.id.player1));
        p2 = (TextView)(rootView.findViewById(R.id.player2));
        t.setText(String.valueOf(timer));
        ts.setText("TopScore: "+156);
        p1.setText(player1 +":"+ score1);
        p2.setText(player2 +":"+ score2);
        p2.setBackgroundColor(Color.MAGENTA);
        updateAllTiles();

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();




    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
    }



    public void startTimer() {

        t.setText(String.valueOf(timer));

        if(timer>0)
        {
            timer--;
            mHandler.postDelayed(runnable,1000);
        }
        else if(timer==0)
        {
            timer=25;
            if(currentPlayer==1)
            {
                currentPlayer=0;

                mDatabase.child("game/"+gameId+"/gameState").setValue(getState());
                p1 = (TextView)(getActivity().findViewById(R.id.player1));
                p1.setBackgroundColor(Color.CYAN);
                p2.setBackgroundColor(Color.LTGRAY);

            }
            else
            {
                currentPlayer=1;
                mDatabase.child("game/"+gameId+"/gameState").setValue(getState());
                p2 = (TextView)(getActivity().findViewById(R.id.player2));
                p2.setBackgroundColor(Color.MAGENTA);
                p1.setBackgroundColor(Color.LTGRAY);
            }

            mHandler.postDelayed(runnable,1000);
        }
    }

    public void loadDictionary()
    {
        try
        {
           String line = null;
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("wordlist.txt")));
           char[] letters;
           int counter=0;
           while ((line = bufferedReader.readLine()) != null)
           {
               counter=counter+1;



               letters=line.toLowerCase().toCharArray();

               wordValues[counter]=encode(letters);

           }
           bufferedReader.close();
           Arrays.sort(wordValues);

        }
        catch (Exception ex) {
           // reading error
        }
        Integer[][] alphabets=new Integer[9][9];



        Random random =new Random();
        int range= nineLetterWords.size()-1;
        for(int i=0;i<9;i++)
        {
           alphabets[i] = nineLetterWords.get(random.nextInt(range));


        }
        setAlphabets(alphabets);
    }

    public void initGame() {

        Log.d("WGS", "init game");
        mEntireBoard = new WordGameTile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new WordGameTile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new WordGameTile(this);
                mSmallTiles[large][small].setPlayer(playBoard[large]);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mLastLarge=-1;
        mLastSmall=-1;
        mEntireBoard.setSubTiles(mLargeTiles);
        setAllUnAvailable();
        setAvailableFromLastMove(mLastLarge, mLastSmall);
    }

    public String getOtherPlayerCT()
    {
        if(me==0)
        {
            return ctplayer2;
        }
        else
        {
            return ctplayer1;
        }
    }

    private void setAllUnAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                WordGameTile tile = mSmallTiles[large][small];
                if(!mCurrent.contains(tile)&& (currentPlayer==tile.getPlayer()))
                {
                    setUnAvailable(large,small);
                }

            }
        }
    }
    private void setUnAvailable(int large,int small) {
        WordGameTile tile = mSmallTiles[large][small];
        if(!(tile.getColorMode()==ColorLocked||tile.getColorMode()==ColorHide))
        {
            tile.setColorMode(ColorUnavailable);
            mAvailable.remove(tile);
        }
    }


    private void setAvailableFromLastMove(int large, int small) {
        if (large != -1) {
            for (int i = 0; i < 9; i++) {

                if(i!=small)
                {
                    WordGameTile tile = mSmallTiles[large][i];
                    if(!mCurrent.contains(tile))
                    {
                        setAvailable(large,i);
                    }

                }

            }
        }
        else if (mAvailable.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                setAvailable(large,small);
            }
        }
    }

    private void setAvailable(int large,int small) {
        WordGameTile tile = mSmallTiles[large][small];
        if(!(tile.getColorMode()==ColorLocked||tile.getColorMode()==ColorHide))
        {
            tile.setColorMode(ColorAvailable[tile.getPlayer()]);
            addToAvailable(tile);

        }
    }

    private void setSelected(int large,int small) {
        WordGameTile tile = mSmallTiles[large][small];
        mCurrent.push(tile);
        tile.setColorMode(ColorSelected);
    }

    private void setLocked(WordGameTile tile) {
        tile.setColorMode(ColorLocked);
        mAvailable.remove(tile);
    }



    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;
        setSelected(large,small);
        setAllUnAvailable();
        setAvailableFromLastMove(large,small);


        updateAllTiles();
    }



    //******************************************************************************************
    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        Random rand =new Random();

        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);
            Integer[] pos = seed[rand.nextInt(10)];
            for (int small = 0; small < 9; small++) {
                ImageButton inner = (ImageButton) outer.findViewById(mSmallIds[pos[small]]);
                final int fLarge = large;
                final int fSmall = pos[small];
                final WordGameTile smallTile = mSmallTiles[large][pos[small]];
                smallTile.setLarge(large);
                smallTile.setSmall(pos[small]);
                smallTile.setView(inner);
                smallTile.setCharlevel(alphabets[large][small]);
                // ...
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // ...
                        if (isAvailable(smallTile)&& (currentPlayer==smallTile.getPlayer())) {
                            smallTile.animate();
                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                            if(mCurrent.contains(smallTile))
                            {
                                if(wordExists())
                                {
                                    for(WordGameTile tile: mCurrent)
                                    {
                                        setLocked(tile);
                                    }
                                    mCurrent.clear();
                                    hideLockedBoard(smallTile);
                                    setAllAvailable();

                                    if(currentPlayer==1) { p1.setText(player1+": "+ score1);}
                                    else{p1.setText(player2+": "+ score2);}

                                    updateAllTiles();
                                }
                                else
                                {
                                    if(currentPlayer==1) {score1= score1- (score1/5);}
                                    else {score2= score2- (score2/5);}

                                    mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                                    mCurrent.clear();
                                    setAllAvailable();
                                    updateAllTiles();

                                }
                            }
                            else if(isAdjacent(fLarge,fSmall))
                            {
                                makeMove(fLarge, fSmall);
                            }

                            mDatabase.child("game/"+gameId+"/gameState").setValue(getState());

                        }
                        else
                        {
                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                        }



                    }



                });
                // ...
            }
        }
    }

    public void hideLockedBoard(WordGameTile smallTile)
    {
        for (int i = 0; i < 9; i++)
        {
           int lockedLarge=smallTile.getLarge();
           WordGameTile tile = mSmallTiles[lockedLarge][i];
           if(!(tile.getColorMode()==ColorLocked))
           {
               mAvailable.remove(tile);
               tile.setColorMode(ColorHide);
           }
        }
    }



    public boolean wordExists() {

        long value =0;
        int wordscore=0;
        String word="";
        for (WordGameTile tile : mCurrent)
        {
          int num = tile.getCharlevel();
           wordscore=wordscore+calScore(num);
           word=word+letters[num-1];
           if(num<10)
           {
               value= value*10+num;
           }
           else
           {
               value=value*100+num;
           }
        }
        if(value==0)
        {
           return false;
        }
        else if(Arrays.binarySearch(wordValues,value)>0)
        {
           if(!wordsDetected.contains(value))
           {
               if(mCurrent.size()==9)
               {
                   wordscore=wordscore*2;

                   for(int i =0; i<9; i++)
                   {
                       WordGameTile tile = mSmallTiles[4][i];
                       tile.setPlayer(currentPlayer);
                   }

               }
               words.add(0,word);
               adapter.notifyDataSetChanged();
               wordsDetected.add(value);

               if(currentPlayer==1) {score1= score1+ wordscore;}
               else {score2= score2+ wordscore;}

               return  true;
           }


        }
        return false;
    }



    public void setAlphabets(Integer[][] alphabets) {
    this.alphabets = alphabets;
    }


    private void addToAvailable(WordGameTile tile) {
        tile.animate();
        mAvailable.add(tile);
    }

    public boolean isAvailable(WordGameTile tile) {
    return mAvailable.contains(tile);
    }



    public boolean isAdjacent(int large, int small){
        if(mCurrent.size()!=0)
        {
           int i= mCurrent.peek().getLarge();
           int j= mCurrent.peek().getSmall();
           return (large==i && isAdjacentHelper(j,small));
        }

        return  true;
    }

    public boolean isAdjacentHelper(int i, int j){

        boolean flag=false;
        switch(i)
        {
            case 0: if(j==1||j==3||j==4){ flag=true;}
                    break;
            case 1: if(j==0||j==2||j==3||j==4||j==5){ flag=true;}
                    break;
            case 2: if(j==1||j==4||j==5){ flag=true;}
                    break;
            case 3: if(j==0||j==1||j==4||j==7||j==6){ flag=true;}
                    break;
            case 4: if(j==0||j==1||j==3||j==7||j==6||j==2||j==5||j==8){ flag=true;}
                    break;
            case 5: if(j==1||j==7||j==4||j==2||j==8){ flag=true;}
                    break;
            case 6: if(j==3||j==7||j==4){ flag=true;}
                    break;
            case 7: if(j==6||j==3||j==4||j==5||j==8){ flag=true;}
                    break;
            case 8: if(j==4||j==5||j==7){ flag=true;}
                break;

        }
        return  flag;

    }



    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
         mLargeTiles[large].updateDrawableState();
         for (int small = 0; small < 9; small++) {
            mSmallTiles[large][small].updateDrawableState();
         }
        }
    }

    /** Create a string containing the state of the game. */
    public String getState() {
        StringBuilder builder = new StringBuilder();
        builder.append(mLastLarge);
        builder.append(',');
        builder.append(mLastSmall);
        builder.append(',');
        builder.append(timer);
        builder.append(',');
        builder.append(currentPlayer);
        builder.append(',');
        builder.append(score1);
        builder.append(',');
        builder.append(score2);

        for (int large = 0; large < 9; large++) {
         for (int small = 0; small < 9; small++) {
            builder.append(',');
            builder.append(mSmallTiles[large][small].getCharlevel());
            builder.append(',');
            builder.append(mSmallTiles[large][small].getColorMode());
         }
        }
        builder.append(',');
        builder.append(wordsDetected.size());
        for(long value: wordsDetected)
        {
          builder.append(',');
          builder.append(value);
        }
        builder.append(',');
        builder.append(mAvailable.size());
        for(WordGameTile tile: mAvailable)
        {
           builder.append(',');
           builder.append(tile.getLarge());
           builder.append(',');
           builder.append(tile.getSmall());
        }
        builder.append(',');
        builder.append(words.size());
        for(String word: words)
        {
           builder.append(',');
           builder.append(word);
        }
        builder.append(',');
        builder.append(mCurrent.size());
        for(WordGameTile tile: mCurrent)
        {
           builder.append(',');
           builder.append(tile.getLarge());
           builder.append(',');
           builder.append(tile.getSmall());
        }
        return builder.toString();
    }



    /** Restore the state of the game from the given string. */
    public void putState(String gameData) {
        if(gameData.equals(""))
        {

        }
        else{

           String[] fields = gameData.split(",");
           int size;
           int index = -1;
           mLastLarge = Integer.parseInt(fields[++index]);
           mLastSmall = Integer.parseInt(fields[++index]);
           timer = Integer.parseInt(fields[++index]);
           currentPlayer = Integer.parseInt(fields[++index]);
           score1= Integer.parseInt(fields[++index]);
           score2= Integer.parseInt(fields[++index]);
           for (int large = 0; large < 9; large++) {
               for (int small = 0; small < 9; small++) {
                   mSmallTiles[large][small].setCharlevel(Integer.parseInt(fields[++index]));
                   mSmallTiles[large][small].setColorMode(Integer.parseInt(fields[++index]));
               }
           }
           size=Integer.parseInt(fields[++index]);
           wordsDetected.clear();
           for(int i=0;i<size;i++)
           {
               wordsDetected.add(Long.parseLong(fields[++index]));

           }

           size=Integer.parseInt(fields[++index]);
           mAvailable.clear();
           for(int i=0;i<size;i++)
           {
               int l=Integer.parseInt(fields[++index]);
               int s=Integer.parseInt(fields[++index]);
               mAvailable.add(mSmallTiles[l][s]);

           }
           size=Integer.parseInt(fields[++index]);
           words.clear();
           for(int i=0;i<size;i++)
           {
               String l=fields[++index];
               words.add(l);
               adapter.notifyDataSetChanged();

           }
           size=Integer.parseInt(fields[++index]);
           mCurrent.clear();
           for(int i=0;i<size;i++)
           {
               int l=Integer.parseInt(fields[++index]);
               int s=Integer.parseInt(fields[++index]);
               mCurrent.add(mSmallTiles[l][s]);

           }





           setAvailableFromLastMove(mLastLarge,mLastSmall);
           t = (TextView)(getActivity().findViewById(R.id.timer));
           p1 = (TextView)(getActivity().findViewById(R.id.player1));
           p2 = (TextView)(getActivity().findViewById(R.id.player2));
           ts = (TextView)(getActivity().findViewById(R.id.topscore));
           t.setText(String.valueOf(timer));
           ts.setText("TopScore - "+156);
           p1.setText(player1+": "+score1);
           p2.setText(player2+": "+score2);
           updateAllTiles() ;
        }

    }



    public long encode(char[] letters)
    {
        long value=0;
        if(letters.length==9)
        {  Integer[] nineLetterWord = new Integer[9];
            int i=0;
            for(char letter:letters)
            {
                switch(letter)
                {
                    case 'a': {value= value*10+1;
                        nineLetterWord[i]= 1;
                        break;}
                    case 'b': {value= value*10+2;
                        nineLetterWord[i]= 2;
                        break;}
                    case 'c': {value= value*10+3;
                        nineLetterWord[i]= 3;
                        break;}
                    case 'd': {value= value*10+4;
                        nineLetterWord[i]= 4;
                        break;}
                    case 'e': {value= value*10+5;
                        nineLetterWord[i]= 5;
                        break;}
                    case 'f': {value= value*10+6;
                        nineLetterWord[i]= 6;
                        break;}
                    case 'g': {value= value*10+7;
                        nineLetterWord[i]= 7;
                        break;}
                    case 'h': {value= value*10+8;
                        nineLetterWord[i]= 8;
                        break;}
                    case 'i': {value= value*10+9;
                        nineLetterWord[i]= 9;
                        break;}
                    case 'j': {value= value*100+10;
                        nineLetterWord[i]= 10;
                        break;}
                    case 'k': {value= value*100+11;
                        nineLetterWord[i]= 11;
                        break;}
                    case 'l': {value= value*100+12;
                        nineLetterWord[i]= 12;
                        break;}
                    case 'm':{value= value*100+13;
                        nineLetterWord[i]= 13;
                        break;}
                    case 'n': {value= value*100+14;
                        nineLetterWord[i]= 14;
                        break;}
                    case 'o': {value= value*100+15;
                        nineLetterWord[i]= 15;
                        break;}
                    case 'p': {value= value*100+16;
                        nineLetterWord[i]= 16;
                        break;}
                    case 'q': {value= value*100+17;
                        nineLetterWord[i]= 17;
                        break;}
                    case 'r': {value= value*100+18;
                        nineLetterWord[i]= 18;
                        break;}
                    case 's': {value= value*100+19;
                        nineLetterWord[i]= 19;
                        break;}
                    case 't': {value= value*100+20;
                        nineLetterWord[i]= 20;
                        break;}
                    case 'u': {value= value*100+21;
                        nineLetterWord[i]= 21;
                        break;}
                    case 'v': {value= value*100+22;
                        nineLetterWord[i]= 22;
                        break;}
                    case 'w': {value= value*100+23;
                        nineLetterWord[i]= 23;
                        break;}
                    case 'x': {value= value*100+24;
                        nineLetterWord[i]= 24;
                        break;}
                    case 'y': {value= value*100+25;
                        nineLetterWord[i]= 25;
                        break;}
                    case 'z': {value= value*100+26;
                        nineLetterWord[i]= 26;
                        break;}


                }

                i++;

            }

            nineLetterWords.add(nineLetterWord);

        }
        else
        {
            for(char letter:letters)
            {
                switch(letter)
                {
                    case 'a': value= value*10+1;break;
                    case 'b': value= value*10+2;break;
                    case 'c': value= value*10+3;break;
                    case 'd': value= value*10+4;break;
                    case 'e': value= value*10+5;break;
                    case 'f': value= value*10+6;break;
                    case 'g': value= value*10+7;break;
                    case 'h': value= value*10+8;break;
                    case 'i': value= value*10+9;break;
                    case 'j': value= value*100+10;break;
                    case 'k': value= value*100+11;break;
                    case 'l': value= value*100+12;break;
                    case 'm': value= value*100+13;break;
                    case 'n': value= value*100+14;break;
                    case 'o': value= value*100+15;break;
                    case 'p': value= value*100+16;break;
                    case 'q': value= value*100+17;break;
                    case 'r': value= value*100+18;break;
                    case 's': value= value*100+19;break;
                    case 't': value= value*100+20;break;
                    case 'u': value= value*100+21;break;
                    case 'v': value= value*100+22;break;
                    case 'w': value= value*100+23;break;
                    case 'x': value= value*100+24;break;
                    case 'y': value= value*100+25;break;
                    case 'z': value= value*100+26;break;


                }



            }

        }

        return value;
    }

    public int  calScore(int num)
    {
        if(num==26||num==17)
        {
            return 10;
        }
        else if(num==24||num==10)
        {
            return 8;
        }
        else if(num==11)
        {
            return 5;
        }
        else if(num==25||num==6||num==8||num==22||num==23)
        {
            return 4;
        }
        else if(num==2||num==3||num==13||num==16)
        {
            return 3;
        }
        else if(num==4||num==7)
        {
            return 2;
        }
        else
        {
            return 1;
        }


        }

    }

