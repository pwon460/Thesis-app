package simo.transport.helpers;

import simo.transport.R;
import simo.transport.backend.MockTransportDAO;
import simo.transport.backend.TransportDAO;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class DAOBuilder {

	private Context ctx;
	private TransportDAO transportDAO = new MockTransportDAO(ctx);

	public DAOBuilder(Context ctx) {
		this.ctx = ctx;
	}

	public void rebuildDAO(Intent intent, String transport) {
		Resources res = ctx.getResources();
		if (transport.equals(res.getString(R.string.bus))) {
			Boolean isBySuburb = intent.getBooleanExtra("isBySuburb", false);
			if (isBySuburb) {
				String originSuburb = intent.getStringExtra("originSuburb");
				String destSuburb = intent.getStringExtra("destSuburb");
				String originStop = intent.getStringExtra("originStop");
				String destStop = intent.getStringExtra("destStop");
				transportDAO.setBySuburbInfo(originSuburb, destSuburb,
						originStop, destStop);
			} else {
				String route = intent.getStringExtra("route");
				String originStop = intent.getStringExtra("originStop");
				String destStop = intent.getStringExtra("destStop");
				transportDAO.setByStopInfo(route, originStop, destStop);
			}
		} else {
			String origin = intent.getStringExtra("origin");
			String destination = intent.getStringExtra("destination");
			transportDAO.setTrip(origin, destination);
		}
	}

	public TransportDAO getDAO() {
		return transportDAO;
	}
}
