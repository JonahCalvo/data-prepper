package com.amazon.dataprepper.plugins.prepper.geoip.provider;

import com.amazon.dataprepper.model.configuration.PluginSetting;
import com.amazon.dataprepper.plugins.prepper.geoip.GeoIpPrepperConfig;

import java.util.List;
import java.util.Objects;

public class GeoIpProviderFactory {
    private static final String[] DEFAULT_DESIRED_FIELDS = new String[]{};

    public GeoIpProvider createGeoIpProvider(final PluginSetting pluginSetting) {
        Objects.requireNonNull(pluginSetting);
        final String providerString = pluginSetting.getStringOrDefault(GeoIpPrepperConfig.DATA_SOURCE, null);
        Objects.requireNonNull(providerString, String.format("Missing '%s' configuration value", GeoIpPrepperConfig.DATA_SOURCE));

        final DataSource provider = DataSource.valueOf(providerString);
        switch (provider) {
            case MaxMindGeolite2CityDatabase:
                String dbPath = pluginSetting.getStringOrDefault(GeoIpPrepperConfig.DATABASE_PATH, null);
                @SuppressWarnings("unchecked")
                final List<String> fields = (List<String>) pluginSetting.getAttributeFromSettings(GeoIpPrepperConfig.DESIRED_FIELDS);
                String[] desiredFields;
                if (fields != null) {
                      desiredFields = fields.toArray(new String[0]);
                } else {
                    desiredFields = new String[]{};
                }
                Objects.requireNonNull(dbPath, "database_path must not be null when provider is MaxMind database");
                return new MaxMindGeoIpProvider(dbPath, desiredFields);
            default:
                throw new UnsupportedOperationException(String.format("Unsupported data source: %s", provider));

        }
    }
}