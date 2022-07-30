package com.pro100svitlo.creditCardNfcReader.utils;

import com.pro100svitlo.creditCardNfcReader.iso7816emv.EmvTags;
import com.pro100svitlo.creditCardNfcReader.model.EmvCard;
import com.pro100svitlo.creditCardNfcReader.model.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.devnied.bitlib.BytesUtils;

/**
 * Extract track data
 */
public final class TrackUtils {

	/**
	 * Class logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackUtils.class);

	/**
	 * Track 2 pattern
	 */
	private static final Pattern TRACK2_PATTERN = Pattern.compile("([0-9]{1,19})D([0-9]{4})([0-9]{3})?(.*)");

	/**
	 * Extract track 2 data
	 * 
	 * @param pEmvCard
	 *            Object card representation
	 * @param pData
	 *            data to parse
	 * @return true if the extraction succeed false otherwise
	 */
	public static boolean extractTrack2Data(final EmvCard pEmvCard, final byte[] pData) {
		boolean ret = false;
		System.out.println("#*# extractTrack2Data using");
		System.out.println("#*# EmvTags.TRACK_2_EQV_DATA:" + EmvTags.TRACK_2_EQV_DATA);
		System.out.println("#*# EmvTags.TRACK_2_DATA:" + EmvTags.TRACK2_DATA);
		byte[] track2 = TlvUtil.getValue(pData, EmvTags.TRACK_2_EQV_DATA, EmvTags.TRACK2_DATA);

		if (track2 != null) {
			System.out.println("#*# extractTrack2Data track2 is NOT NULL");
			System.out.println("#*# extractTrack2Data data:");
			String data = BytesUtils.bytesToStringNoSpace(track2);
			System.out.println("#*# " + data);
			Matcher m = TRACK2_PATTERN.matcher(data);
			System.out.println("#*# matcher " + m);
			System.out.println("#*# group 1 " + m.group(1));
			System.out.println("#*# group 2 " + m.group(2));
			System.out.println("#*# group 3 " + m.group(3));
			// Check pattern
			if (m.find()) {
				// read card number
				pEmvCard.setCardNumber(m.group(1));
				// Read expire date
				String month = m.group(2).substring(2,4);
				String year = m.group(2).substring(0,2);
				pEmvCard.setExpireDate(month+"/"+year);
				// Read service
				pEmvCard.setService(new Service(m.group(3)));
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Private constructor
	 */
	private TrackUtils() {
	}

}
