package com.example.mkostiuk.android_audio_player.upnp;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;

/**
 * Created by mkostiuk on 23/06/2017.
 */

public class ServiceUpnp {
    private AndroidUpnpService upnpService;
    private UDN udnRecorder;
    private ServiceConnection serviceConnection;


    public ServiceUpnp() {


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                upnpService = (AndroidUpnpService) service;


                LocalService<AudioFileService> remoteControllerService = getRecorderLocalService();

                // Register the device when this activity binds to the service for the first time
                if (remoteControllerService == null) {
                    try {
                        System.err.println("CREATION DEVICE!!!");
                        udnRecorder = new SaveUDN().getUdn();
                        LocalDevice remoteDevice = AudioPlayerDevice.createDevice(udnRecorder);

                        upnpService.getRegistry().addDevice(remoteDevice);

                    } catch (Exception ex) {
                        System.err.println("Creating Android remote controller device failed !!!");
                        return;
                    }
                }

                System.out.println("Creation device reussie...");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                upnpService = null;
            }
        };
    }

    public LocalService<AudioFileService> getRecorderLocalService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnRecorder, true)) == null)
            return null;

        return (LocalService<AudioFileService>)
                remoteDevice.findService(new UDAServiceType("AudioFileService", 1));
    }

    public ServiceConnection getService() {
        return serviceConnection;
    }

    public void stop() {
        upnpService.get().shutdown();
    }
}
