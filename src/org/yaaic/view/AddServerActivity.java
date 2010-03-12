/*
Yaaic - Yet Another Android IRC Client

Copyright 2009 Sebastian Kaspari

This file is part of Yaaic.

Yaaic is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Yaaic is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Yaaic.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.yaaic.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.yaaic.R;
import org.yaaic.Yaaic;
import org.yaaic.db.Database;
import org.yaaic.model.Server;
import org.yaaic.model.Status;

/**
 * Add a new server to the list
 * 
 * @author Sebastian Kaspari <sebastian@yaaic.org>
 */
public class AddServerActivity extends Activity implements OnClickListener
{
	public static final String TAG = "Yaaic/AddServerActivity";
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.serveradd);
        
        ((Button) findViewById(R.id.add)).setOnClickListener(this);
        ((Button) findViewById(R.id.cancel)).setOnClickListener(this);
    }

	/**
	 * On click add server or cancel activity
	 */
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.add:
				// server
				String title = ((EditText) findViewById(R.id.title)).getText().toString();
				String host = ((EditText) findViewById(R.id.host)).getText().toString();
				int port = Integer.parseInt(((EditText) findViewById(R.id.port)).getText().toString());
				String password = ((EditText) findViewById(R.id.password)).getText().toString();
				boolean autoConnect = ((CheckBox) findViewById(R.id.autoconnect)).isChecked();
				boolean useSSL = ((CheckBox) findViewById(R.id.useSSL)).isChecked();
				
				// identity
				String nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
				String ident = ((EditText) findViewById(R.id.ident)).getText().toString();
				String realname = ((EditText) findViewById(R.id.realname)).getText().toString();
				
								
				Database db = new Database(this);
				long identityId = db.addIdentity(nickname, ident, realname);
				
				Log.d(TAG, "New Identity with Id " + identityId + " (" + nickname + ", " + ident + ", " + realname + ")");
				
				long serverId = db.addServer(title, host, port, password, autoConnect, useSSL, identityId);
				db.close();
				
				Server server = new Server();
				server.setId((int) serverId);
				server.setHost(host);
				server.setPort(port);
				server.setTitle(title);
				server.setStatus(Status.DISCONNECTED);
				Yaaic.getInstance().addServer(server);
				
				Log.d(TAG, "Saved server " + title);

				setResult(RESULT_OK);
				finish();
			break;
			case R.id.cancel:
				setResult(RESULT_CANCELED);
				finish();
			break;
		}
	}
}
