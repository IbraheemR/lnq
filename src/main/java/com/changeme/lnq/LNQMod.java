package com.changeme.lnq;

import com.changeme.lnq.commands.SuicideCommand;
import com.changeme.lnq.commands.TPSCommand;
import com.changeme.lnq.restapi.RestAPI;
import com.changeme.lnq.util.ModUtil;
import net.fabricmc.api.ModInitializer;


public class LNQMod implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println(
				String.format(
						"LNQ v%s UP! :)",
						ModUtil.getLNQModMetadata().getVersion().getFriendlyString()
				)
		);

		SuicideCommand.init();

		TPSCommand.init();
		CustomPlayerList.init();

		RestAPI.init();
	}
}
