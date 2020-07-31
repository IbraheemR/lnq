package com.changeme.lnq;

import com.changeme.lnq.commands.SuicideCommand;
import com.changeme.lnq.commands.TPSCommand;
import net.fabricmc.api.ModInitializer;


public class LNQMod implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("LNQ UP! :)");

		SuicideCommand.register();

		TPSCommand.register();
		CustomPlayerList.register();
	}
}
