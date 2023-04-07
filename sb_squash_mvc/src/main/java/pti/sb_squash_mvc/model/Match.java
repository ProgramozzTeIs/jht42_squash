package pti.sb_squash_mvc.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "match")
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "player1id")
    private int player1Id;
    
    @Transient
    private User player1;
    
    @Column(name = "player2id")
    private int player2Id;
    
    @Transient
    private User player2;
    
    @Column(name = "courtid")
    private int courtId;
    
    @Transient
    private Court court;
    
    @Column(name = "scoreplayer1")
    private int scorePlayer1;
    
    @Column(name = "scoreplayer2")
    private int scorePlayer2;
    
    @Column(name = "date")
    private LocalDateTime date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public void setScorePlayer1(int scorePlayer1) {
        this.scorePlayer1 = scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

    public void setScorePlayer2(int scorePlayer2) {
        this.scorePlayer2 = scorePlayer2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Match{" + "id=" + id + ", player1Id=" + player1Id + ", player1=" + player1 + ", player2Id=" + player2Id + ", player2=" + player2 + ", courtId=" + courtId + ", court=" + court + ", scorePlayer1=" + scorePlayer1 + ", scorePlayer2=" + scorePlayer2 + ", date=" + date + '}';
    }
    
}
