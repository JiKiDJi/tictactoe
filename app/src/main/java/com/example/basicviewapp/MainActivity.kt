package com.example.basicviewapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var player1: TextView
    private lateinit var player2: TextView
    private lateinit var player1Name: EditText
    private lateinit var player2Name: EditText
    private lateinit var gameInfo: Array<TextView>
    private lateinit var buttons: Array<Button>
    private lateinit var ngButton: Button
    private lateinit var reset: Button
    private lateinit var radioP: RadioGroup
    private lateinit var radioO: RadioGroup
    private var firstPlayer = 1
    private var curPlayer = 1
    private var p1 = "P1"
    private var p2 = "P2"
    private var sqVals = IntArray(9) { -1 }
    private var gameWon = false
    private var results = IntArray(3) { 0 }
    private var PvC = false
    private var computerMove = false



    private fun updateInfo(){
        gameInfo.forEachIndexed { index, info ->
            info.text = results[index].toString()
        }
    }

    private fun reset(){
        results = IntArray(3) { 0 }
        updateInfo()
    }

    private fun popUp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welcome!")
        builder.setMessage("Hello by Alekss Verškovs")

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun newGame (){
        computerMove = false
        firstPlayer = 1
        curPlayer = 1
        sqVals = IntArray(9) { -1 }
        buttons.forEachIndexed { _, button ->
            button.text = ""
        }
        gameWon = false
        textView.text = ""

        p1 = "${player1Name.text}"

        if(p1.length == 0)
            p1 = "P1"

        player1.text = "$p1:"

        var id: Int = radioP.getCheckedRadioButtonId()

        if(id==R.id.radioPvC){
            if(PvC == false) reset()
            PvC = true
            id = radioO.getCheckedRadioButtonId()
            if(id==R.id.radioSecond){
                firstPlayer = 0
                computerMove = true
                while(true){
                    val rand = (0..8).random()
                    if(sqVals[rand]==-1){
                        changeValue(rand)
                        break
                    }
                }
            }
            p2 = "Computer"
            player2.text = "$p2:"
        }
        else{
            p2 = "${player2Name.text}"
            if(p2.length == 0)
                p2 = "P2"

            if(PvC) reset()
            PvC = false
            id = radioO.getCheckedRadioButtonId()
            if(id==R.id.radioSecond)
                firstPlayer = 0
        }
        player2.text = "$p2:"
    }
    @SuppressLint("SetTextI18n")
    fun checkForWin(i: Int, v: Int){
        val b: Int = i%3
        val a: Int = i-b

        if(sqVals[a]==sqVals[a+1] && sqVals[a]==sqVals[a+2] || sqVals[b]==sqVals[b+3] && sqVals[b]==sqVals[b+6]) {
            gameWon = true
        }

        else if(i%2==0 && sqVals[4]==v){
            if(sqVals[0]==v && sqVals[8]==v || sqVals[2]==v && sqVals[6]==v){
                gameWon = true
            }
        }
        if(gameWon){
            if(v == firstPlayer) {
                textView.text = "$p1 has won"
                results[0]+=1
            }
            else {
                textView.text = "$p2 has won"
                results[1]+=1
            }
            updateInfo()
        }
        else{
            for(j in 0..8){
                if(sqVals[j]==-1)
                    return
            }
            results[2]+=1
            gameWon = true
            textView.text = "Draw"
            updateInfo()
        }
    }
    private fun changeValue(i: Int){
        if(sqVals[i]!=-1)
            return

        sqVals[i]=curPlayer

        if(sqVals[i]==0)
            buttons[i].text = "O"
        else
            buttons[i].text = "X"

        checkForWin(i,curPlayer)

        curPlayer = (curPlayer+1)%2

        computerMove = !computerMove

        if(PvC && computerMove && !gameWon)
            while(true){
                val rand = (0..8).random()
                if(sqVals[rand]==-1){
                    changeValue(rand)
                    break
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popUp()

        textView = findViewById(R.id.textfield)
        ngButton = findViewById(R.id.ng_button)
        player1 = findViewById(R.id.player1)
        player2 = findViewById(R.id.player2)
        player1Name = findViewById(R.id.p1_input)
        player2Name = findViewById(R.id.p2_input)
        reset = findViewById(R.id.reset)

        buttons = arrayOf(findViewById(R.id.button0), findViewById(R.id.button1),
            findViewById(R.id.button2), findViewById(R.id.button3),
            findViewById(R.id.button4), findViewById(R.id.button5),
            findViewById(R.id.button6), findViewById(R.id.button7),
            findViewById(R.id.button8))
        gameInfo = arrayOf(findViewById(R.id.player1_info), findViewById(R.id.player2_info),
            findViewById(R.id.draw_info))

        updateInfo()

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if(!gameWon)
                    changeValue(index)
            }
        }

        radioP = findViewById(R.id.radioGroup1)
        radioO = findViewById(R.id.radioGroup2)


        reset.setOnClickListener {
            reset()
        }

        ngButton.setOnClickListener {
            newGame()
        }
    }

}