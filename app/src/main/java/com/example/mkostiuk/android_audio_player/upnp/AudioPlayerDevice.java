package com.example.mkostiuk.android_audio_player.upnp;

import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

/**
 * Created by mkostiuk on 23/06/2017.
 */

public class AudioPlayerDevice {
    static LocalDevice createDevice(UDN udn)
            throws ValidationException, LocalServiceBindingException {

        DeviceType type =
                new UDADeviceType("AudioPlayer", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Audio Player",
                        new ManufacturerDetails("IRIT"),
                        new ModelDetails("AndroidController", "Lit un fichier audio", "v1")
                );

        LocalService service =
                new AnnotationLocalServiceBinder().read(AudioFileService.class);

        service.setManager(
                new DefaultServiceManager<>(service, AudioFileService.class)
        );

        return new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,

                service
        );
    }
}
