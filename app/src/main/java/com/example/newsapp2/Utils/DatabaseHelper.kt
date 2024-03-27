package com.example.newsapp2.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.newsapp2.Model.BookMarkModel.ArticleModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BookmarkDB"
        private const val TABLE_NAME = "Bookmark"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_URL = "url"
        private const val COLUMN_URL_TO_IMAGE = "urlToImage"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, $COLUMN_URL TEXT, $COLUMN_URL_TO_IMAGE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //save data passed from button on click listner
    fun insertArticle(title: String, url: String, urlToImage: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_URL, url)
            put(COLUMN_URL_TO_IMAGE, urlToImage)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun deleteArticle(url: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_URL = ?", arrayOf(url))
    }

    fun isArticleBookmarked(url: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_URL = ?", arrayOf(url))
        val isBookmarked = cursor.count > 0
        cursor.close()
        return isBookmarked
    }


    //retrive data in bookmarkfragment
    @SuppressLint("Range")
    fun getAllArticles(): ArrayList<ArticleModel> {
        val articleList = ArrayList<ArticleModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor?.let {
            while (cursor.moveToNext()) {
                val article = ArticleModel(
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    url = cursor.getString(cursor.getColumnIndex(COLUMN_URL)),
                    urlToImage = cursor.getString(cursor.getColumnIndex(COLUMN_URL_TO_IMAGE))
                )
                articleList.add(article)
            }
            cursor.close()
        }
        return articleList
    }
}