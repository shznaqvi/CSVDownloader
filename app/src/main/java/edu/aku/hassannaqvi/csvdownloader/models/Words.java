package edu.aku.hassannaqvi.csvdownloader.models;

import android.database.Cursor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.aku.hassannaqvi.csvdownloader.interfaces.WordsContract.WordsTable;

public class Words {

    private final static long serialVersionUID = 5737885947751540603L;
    private String id;
    private String word;
    private String trans;
    private String sentcol1;
    private String sentcol2;
    private String sentcol3;
    private String grade;
    private String category;
    private String pos;
    private int viewCount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Words() {
    }

    /**
     * @param pos
     * @param grade
     * @param sentcol2
     * @param sentcol3
     * @param id
     * @param sentcol1
     * @param category
     * @param word
     * @param trans
     */
    public Words(String id, String word, String trans, String sentcol1, String sentcol2, String sentcol3, String grade, String category, String pos) {
        super();
        this.id = id;
        this.word = word;
        this.trans = trans;
        this.sentcol1 = sentcol1;
        this.sentcol2 = sentcol2;
        this.sentcol3 = sentcol3;
        this.grade = grade;
        this.category = category;
        this.pos = pos;
    }


    public Words Hydrate(JSONObject word) throws JSONException {

        this.id = word.getString("id");
        this.word = word.getString("word");
        this.trans = word.getString("trans");
        this.sentcol1 = word.getString("sentcol1");
        this.sentcol2 = word.getString("sentcol2");
        this.sentcol3 = word.getString("sentcol3");
        this.grade = word.getString("grade");
        this.category = word.getString("category");
        this.pos = word.getString("pos");

        return this;
    }

    public Words Hydrate(Cursor cursor) throws JSONException {

        this.id = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_ID));
        this.word = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_WORD));
        this.trans = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_TRANS));
        this.sentcol1 = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_S1));
        this.sentcol2 = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_S2));
        this.sentcol3 = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_S3));
        this.grade = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_GRADE));
        this.category = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_CATEGORY));
        this.pos = cursor.getString(cursor.getColumnIndex(WordsTable.COLUMN_POS));

        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Words withId(String id) {
        this.id = id;
        return this;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Words withWord(String word) {
        this.word = word;
        return this;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public Words withTrans(String trans) {
        this.trans = trans;
        return this;
    }

    public String getSentcol1() {
        return sentcol1;
    }

    public void setSentcol1(String sentcol1) {
        this.sentcol1 = sentcol1;
    }

    public Words withSentcol1(String sentcol1) {
        this.sentcol1 = sentcol1;
        return this;
    }

    public String getSentcol2() {
        return sentcol2;
    }

    public void setSentcol2(String sentcol2) {
        this.sentcol2 = sentcol2;
    }

    public Words withSentcol2(String sentcol2) {
        this.sentcol2 = sentcol2;
        return this;
    }

    public String getSentcol3() {
        return sentcol3;
    }

    public void setSentcol3(String sentcol3) {
        this.sentcol3 = sentcol3;
    }

    public Words withSentcol3(String sentcol3) {
        this.sentcol3 = sentcol3;
        return this;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Words withGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Words withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Words withPos(String pos) {
        this.pos = pos;
        return this;
    }

    public int getViewCount() {
        return viewCount;
    }

    public Words setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Words withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("word", word).append("trans", trans).append("sentcol1", sentcol1).append("sentcol2", sentcol2).append("sentcol3", sentcol3).append("grade", grade).append("category", category).append("pos", pos).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pos).append(grade).append(sentcol2).append(sentcol3).append(id).append(sentcol1).append(additionalProperties).append(category).append(word).append(trans).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Words) == false) {
            return false;
        }
        Words rhs = ((Words) other);
        return new EqualsBuilder().append(pos, rhs.pos).append(grade, rhs.grade).append(sentcol2, rhs.sentcol2).append(sentcol3, rhs.sentcol3).append(id, rhs.id).append(sentcol1, rhs.sentcol1).append(additionalProperties, rhs.additionalProperties).append(category, rhs.category).append(word, rhs.word).append(trans, rhs.trans).isEquals();
    }

}
