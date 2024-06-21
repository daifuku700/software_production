import whisper
import sqlite3
import time


if __name__ == "__main__":
    model = whisper.load_model("base")  # モデル指定
    dbname = "server/database.db"

    while True:
        conn = sqlite3.connect(dbname)
        cur = conn.cursor()
        cur.execute("SELECT * FROM chat WHERE description = ''")
        result = []
        for row in cur.fetchall():
            result = model.transcribe(row[2], verbose=True, fp16=False, language="ja")
            cur.execute(
                "UPDATE chat SET description = ? WHERE id = ?", (result["text"], row[0])
            )

        conn.commit()
        cur.close()
        conn.close()
        time.sleep(1)
