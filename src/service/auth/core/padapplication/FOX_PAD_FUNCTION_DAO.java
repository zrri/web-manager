package service.auth.core.padapplication;

import java.util.List;

import both.db.util.DbServiceUtil;
import both.common.util.LoggerUtil;

public class FOX_PAD_FUNCTION_DAO {

	public static int insert(FOX_PAD_FUNCTION_DBO dbo) {
		try {
			int result = DbServiceUtil.executeInsert(dbo);
			return result;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return -1;
	}

	public static int update(FOX_PAD_FUNCTION_DBO dbo,
			FOX_PAD_FUNCTION_DBO wheredbo) {
		try {
			int result = DbServiceUtil.executeUpdate(dbo, wheredbo);
			return result;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return -1;
	}

	public static int delete(FOX_PAD_FUNCTION_DBO dbo) {
		try {
			int result = DbServiceUtil.executeDelete(dbo);
			return result;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return -1;
	}

	public static FOX_PAD_FUNCTION_DBO query(FOX_PAD_FUNCTION_DBO dbo) {
		try {
			List<FOX_PAD_FUNCTION_DBO> list = DbServiceUtil.executeQuery(dbo);
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return null;
	}

	public static List<FOX_PAD_FUNCTION_DBO> queryTable(
			FOX_PAD_FUNCTION_DBO dbo) {
		try {
			List<FOX_PAD_FUNCTION_DBO> list = DbServiceUtil.executeQuery(dbo);
			return list;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return null;
	}
}