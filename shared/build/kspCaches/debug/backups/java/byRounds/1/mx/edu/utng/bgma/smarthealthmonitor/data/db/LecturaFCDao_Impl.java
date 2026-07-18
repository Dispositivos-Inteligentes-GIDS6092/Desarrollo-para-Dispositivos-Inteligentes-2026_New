package mx.edu.utng.bgma.smarthealthmonitor.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LecturaFCDao_Impl implements LecturaFCDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LecturaFC> __insertionAdapterOfLecturaFC;

  private final SharedSQLiteStatement __preparedStmtOfMarcarSincronizado;

  private final SharedSQLiteStatement __preparedStmtOfLimpiarViejos;

  private final EntityUpsertionAdapter<LecturaFC> __upsertionAdapterOfLecturaFC;

  public LecturaFCDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLecturaFC = new EntityInsertionAdapter<LecturaFC>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `lecturas_fc` (`id`,`valorBpm`,`timestamp`,`hora`,`fecha`,`esNormal`,`estado`,`dispositivo`,`sincronizado`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LecturaFC entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getValorBpm());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getHora());
        statement.bindString(5, entity.getFecha());
        final int _tmp = entity.getEsNormal() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindString(7, entity.getEstado());
        statement.bindString(8, entity.getDispositivo());
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    };
    this.__preparedStmtOfMarcarSincronizado = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE lecturas_fc SET sincronizado = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfLimpiarViejos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM lecturas_fc WHERE timestamp < ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfLecturaFC = new EntityUpsertionAdapter<LecturaFC>(new EntityInsertionAdapter<LecturaFC>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `lecturas_fc` (`id`,`valorBpm`,`timestamp`,`hora`,`fecha`,`esNormal`,`estado`,`dispositivo`,`sincronizado`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LecturaFC entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getValorBpm());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getHora());
        statement.bindString(5, entity.getFecha());
        final int _tmp = entity.getEsNormal() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindString(7, entity.getEstado());
        statement.bindString(8, entity.getDispositivo());
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    }, new EntityDeletionOrUpdateAdapter<LecturaFC>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `lecturas_fc` SET `id` = ?,`valorBpm` = ?,`timestamp` = ?,`hora` = ?,`fecha` = ?,`esNormal` = ?,`estado` = ?,`dispositivo` = ?,`sincronizado` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LecturaFC entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getValorBpm());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindString(4, entity.getHora());
        statement.bindString(5, entity.getFecha());
        final int _tmp = entity.getEsNormal() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindString(7, entity.getEstado());
        statement.bindString(8, entity.getDispositivo());
        final int _tmp_1 = entity.getSincronizado() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getId());
      }
    });
  }

  @Override
  public Object insertar(final LecturaFC lectura, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLecturaFC.insert(lectura);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object marcarSincronizado(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarcarSincronizado.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarcarSincronizado.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object limpiarViejos(final long limite, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfLimpiarViejos.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limite);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfLimpiarViejos.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsert(final LecturaFC lectura, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfLecturaFC.upsert(lectura);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LecturaFC>> obtenerUltimas() {
    final String _sql = "SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"lecturas_fc"}, new Callable<List<LecturaFC>>() {
      @Override
      @NonNull
      public List<LecturaFC> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfValorBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBpm");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "esNormal");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final List<LecturaFC> _result = new ArrayList<LecturaFC>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LecturaFC _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpValorBpm;
            _tmpValorBpm = _cursor.getInt(_cursorIndexOfValorBpm);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpHora;
            _tmpHora = _cursor.getString(_cursorIndexOfHora);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final boolean _tmpEsNormal;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEsNormal);
            _tmpEsNormal = _tmp != 0;
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpDispositivo;
            _tmpDispositivo = _cursor.getString(_cursorIndexOfDispositivo);
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            _item = new LecturaFC(_tmpId,_tmpValorBpm,_tmpTimestamp,_tmpHora,_tmpFecha,_tmpEsNormal,_tmpEstado,_tmpDispositivo,_tmpSincronizado);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object obtenerNoSincronizados(final Continuation<? super List<LecturaFC>> $completion) {
    final String _sql = "SELECT * FROM lecturas_fc WHERE sincronizado = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LecturaFC>>() {
      @Override
      @NonNull
      public List<LecturaFC> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfValorBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBpm");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "esNormal");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final List<LecturaFC> _result = new ArrayList<LecturaFC>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LecturaFC _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpValorBpm;
            _tmpValorBpm = _cursor.getInt(_cursorIndexOfValorBpm);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpHora;
            _tmpHora = _cursor.getString(_cursorIndexOfHora);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final boolean _tmpEsNormal;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEsNormal);
            _tmpEsNormal = _tmp != 0;
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpDispositivo;
            _tmpDispositivo = _cursor.getString(_cursorIndexOfDispositivo);
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            _item = new LecturaFC(_tmpId,_tmpValorBpm,_tmpTimestamp,_tmpHora,_tmpFecha,_tmpEsNormal,_tmpEstado,_tmpDispositivo,_tmpSincronizado);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object obtenerPorId(final int id, final Continuation<? super LecturaFC> $completion) {
    final String _sql = "SELECT * FROM lecturas_fc WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LecturaFC>() {
      @Override
      @Nullable
      public LecturaFC call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfValorBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBpm");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "esNormal");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final LecturaFC _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpValorBpm;
            _tmpValorBpm = _cursor.getInt(_cursorIndexOfValorBpm);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpHora;
            _tmpHora = _cursor.getString(_cursorIndexOfHora);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final boolean _tmpEsNormal;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEsNormal);
            _tmpEsNormal = _tmp != 0;
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpDispositivo;
            _tmpDispositivo = _cursor.getString(_cursorIndexOfDispositivo);
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            _result = new LecturaFC(_tmpId,_tmpValorBpm,_tmpTimestamp,_tmpHora,_tmpFecha,_tmpEsNormal,_tmpEstado,_tmpDispositivo,_tmpSincronizado);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object obtenerUltimos5(final Continuation<? super List<LecturaFC>> $completion) {
    final String _sql = "SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LecturaFC>>() {
      @Override
      @NonNull
      public List<LecturaFC> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfValorBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "valorBpm");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfHora = CursorUtil.getColumnIndexOrThrow(_cursor, "hora");
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "esNormal");
          final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
          final int _cursorIndexOfDispositivo = CursorUtil.getColumnIndexOrThrow(_cursor, "dispositivo");
          final int _cursorIndexOfSincronizado = CursorUtil.getColumnIndexOrThrow(_cursor, "sincronizado");
          final List<LecturaFC> _result = new ArrayList<LecturaFC>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LecturaFC _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpValorBpm;
            _tmpValorBpm = _cursor.getInt(_cursorIndexOfValorBpm);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpHora;
            _tmpHora = _cursor.getString(_cursorIndexOfHora);
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final boolean _tmpEsNormal;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEsNormal);
            _tmpEsNormal = _tmp != 0;
            final String _tmpEstado;
            _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
            final String _tmpDispositivo;
            _tmpDispositivo = _cursor.getString(_cursorIndexOfDispositivo);
            final boolean _tmpSincronizado;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSincronizado);
            _tmpSincronizado = _tmp_1 != 0;
            _item = new LecturaFC(_tmpId,_tmpValorBpm,_tmpTimestamp,_tmpHora,_tmpFecha,_tmpEsNormal,_tmpEstado,_tmpDispositivo,_tmpSincronizado);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object contarRegistros(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM lecturas_fc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
