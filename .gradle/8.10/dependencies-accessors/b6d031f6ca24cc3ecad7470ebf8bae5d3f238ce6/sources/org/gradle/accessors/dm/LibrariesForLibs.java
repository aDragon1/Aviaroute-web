package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class VersionAccessors extends VersionFactory  {

        private final CoroutinesVersionAccessors vaccForCoroutinesVersionAccessors = new CoroutinesVersionAccessors(providers, config);
        private final DotenvVersionAccessors vaccForDotenvVersionAccessors = new DotenvVersionAccessors(providers, config);
        private final ExposedVersionAccessors vaccForExposedVersionAccessors = new ExposedVersionAccessors(providers, config);
        private final H2VersionAccessors vaccForH2VersionAccessors = new H2VersionAccessors(providers, config);
        private final JsonVersionAccessors vaccForJsonVersionAccessors = new JsonVersionAccessors(providers, config);
        private final KotlinVersionAccessors vaccForKotlinVersionAccessors = new KotlinVersionAccessors(providers, config);
        private final KotlinxVersionAccessors vaccForKotlinxVersionAccessors = new KotlinxVersionAccessors(providers, config);
        private final KtorVersionAccessors vaccForKtorVersionAccessors = new KtorVersionAccessors(providers, config);
        private final LogbackVersionAccessors vaccForLogbackVersionAccessors = new LogbackVersionAccessors(providers, config);
        private final OpenpdfVersionAccessors vaccForOpenpdfVersionAccessors = new OpenpdfVersionAccessors(providers, config);
        private final PostgresVersionAccessors vaccForPostgresVersionAccessors = new PostgresVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>postgresql</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPostgresql() { return getVersion("postgresql"); }

        /**
         * Group of versions at <b>versions.coroutines</b>
         */
        public CoroutinesVersionAccessors getCoroutines() {
            return vaccForCoroutinesVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.dotenv</b>
         */
        public DotenvVersionAccessors getDotenv() {
            return vaccForDotenvVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.exposed</b>
         */
        public ExposedVersionAccessors getExposed() {
            return vaccForExposedVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.h2</b>
         */
        public H2VersionAccessors getH2() {
            return vaccForH2VersionAccessors;
        }

        /**
         * Group of versions at <b>versions.json</b>
         */
        public JsonVersionAccessors getJson() {
            return vaccForJsonVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.kotlin</b>
         */
        public KotlinVersionAccessors getKotlin() {
            return vaccForKotlinVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.kotlinx</b>
         */
        public KotlinxVersionAccessors getKotlinx() {
            return vaccForKotlinxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor</b>
         */
        public KtorVersionAccessors getKtor() {
            return vaccForKtorVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.logback</b>
         */
        public LogbackVersionAccessors getLogback() {
            return vaccForLogbackVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.openpdf</b>
         */
        public OpenpdfVersionAccessors getOpenpdf() {
            return vaccForOpenpdfVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.postgres</b>
         */
        public PostgresVersionAccessors getPostgres() {
            return vaccForPostgresVersionAccessors;
        }

    }

    public static class CoroutinesVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public CoroutinesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>coroutines</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("coroutines"); }

        /**
         * Version alias <b>coroutines.version</b> with value <b>1.7.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("coroutines.version"); }

    }

    public static class DotenvVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public DotenvVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>dotenv</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("dotenv"); }

        /**
         * Version alias <b>dotenv.version</b> with value <b>5.2.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("dotenv.version"); }

    }

    public static class ExposedVersionAccessors extends VersionFactory  {

        public ExposedVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>exposed.core</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("exposed.core"); }

        /**
         * Version alias <b>exposed.dao</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDao() { return getVersion("exposed.dao"); }

        /**
         * Version alias <b>exposed.datetime</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDatetime() { return getVersion("exposed.datetime"); }

        /**
         * Version alias <b>exposed.jdbc</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJdbc() { return getVersion("exposed.jdbc"); }

        /**
         * Version alias <b>exposed.version</b> with value <b>0.60.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("exposed.version"); }

    }

    public static class H2VersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public H2VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>h2</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("h2"); }

        /**
         * Version alias <b>h2.version</b> with value <b>2.3.232</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("h2.version"); }

    }

    public static class JsonVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public JsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>json</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("json"); }

        /**
         * Version alias <b>json.version</b> with value <b>20250107</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("json.version"); }

    }

    public static class KotlinVersionAccessors extends VersionFactory  {

        private final KotlinPluginVersionAccessors vaccForKotlinPluginVersionAccessors = new KotlinPluginVersionAccessors(providers, config);
        private final KotlinTestVersionAccessors vaccForKotlinTestVersionAccessors = new KotlinTestVersionAccessors(providers, config);
        public KotlinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kotlin.jvm</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJvm() { return getVersion("kotlin.jvm"); }

        /**
         * Version alias <b>kotlin.version</b> with value <b>2.1.10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("kotlin.version"); }

        /**
         * Group of versions at <b>versions.kotlin.plugin</b>
         */
        public KotlinPluginVersionAccessors getPlugin() {
            return vaccForKotlinPluginVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.kotlin.test</b>
         */
        public KotlinTestVersionAccessors getTest() {
            return vaccForKotlinTestVersionAccessors;
        }

    }

    public static class KotlinPluginVersionAccessors extends VersionFactory  {

        public KotlinPluginVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kotlin.plugin.serialization</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSerialization() { return getVersion("kotlin.plugin.serialization"); }

    }

    public static class KotlinTestVersionAccessors extends VersionFactory  {

        public KotlinTestVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kotlin.test.junit</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("kotlin.test.junit"); }

    }

    public static class KotlinxVersionAccessors extends VersionFactory  {

        private final KotlinxHtmlVersionAccessors vaccForKotlinxHtmlVersionAccessors = new KotlinxHtmlVersionAccessors(providers, config);
        public KotlinxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.kotlinx.html</b>
         */
        public KotlinxHtmlVersionAccessors getHtml() {
            return vaccForKotlinxHtmlVersionAccessors;
        }

    }

    public static class KotlinxHtmlVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public KotlinxHtmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kotlinx.html</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("kotlinx.html"); }

        /**
         * Version alias <b>kotlinx.html.version</b> with value <b>0.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("kotlinx.html.version"); }

    }

    public static class KtorVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        private final KtorClientVersionAccessors vaccForKtorClientVersionAccessors = new KtorClientVersionAccessors(providers, config);
        private final KtorSerializationVersionAccessors vaccForKtorSerializationVersionAccessors = new KtorSerializationVersionAccessors(providers, config);
        private final KtorServerVersionAccessors vaccForKtorServerVersionAccessors = new KtorServerVersionAccessors(providers, config);
        public KtorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("ktor"); }

        /**
         * Version alias <b>ktor.version</b> with value <b>3.0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("ktor.version"); }

        /**
         * Group of versions at <b>versions.ktor.client</b>
         */
        public KtorClientVersionAccessors getClient() {
            return vaccForKtorClientVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.serialization</b>
         */
        public KtorSerializationVersionAccessors getSerialization() {
            return vaccForKtorSerializationVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server</b>
         */
        public KtorServerVersionAccessors getServer() {
            return vaccForKtorServerVersionAccessors;
        }

    }

    public static class KtorClientVersionAccessors extends VersionFactory  {

        private final KtorClientContentVersionAccessors vaccForKtorClientContentVersionAccessors = new KtorClientContentVersionAccessors(providers, config);
        public KtorClientVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.client.cio</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCio() { return getVersion("ktor.client.cio"); }

        /**
         * Version alias <b>ktor.client.core</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("ktor.client.core"); }

        /**
         * Group of versions at <b>versions.ktor.client.content</b>
         */
        public KtorClientContentVersionAccessors getContent() {
            return vaccForKtorClientContentVersionAccessors;
        }

    }

    public static class KtorClientContentVersionAccessors extends VersionFactory  {

        public KtorClientContentVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.client.content.negotiation</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNegotiation() { return getVersion("ktor.client.content.negotiation"); }

    }

    public static class KtorSerializationVersionAccessors extends VersionFactory  {

        private final KtorSerializationKotlinxVersionAccessors vaccForKtorSerializationKotlinxVersionAccessors = new KtorSerializationKotlinxVersionAccessors(providers, config);
        public KtorSerializationVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.ktor.serialization.kotlinx</b>
         */
        public KtorSerializationKotlinxVersionAccessors getKotlinx() {
            return vaccForKtorSerializationKotlinxVersionAccessors;
        }

    }

    public static class KtorSerializationKotlinxVersionAccessors extends VersionFactory  {

        public KtorSerializationKotlinxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.serialization.kotlinx.json</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJson() { return getVersion("ktor.serialization.kotlinx.json"); }

    }

    public static class KtorServerVersionAccessors extends VersionFactory  {

        private final KtorServerCallVersionAccessors vaccForKtorServerCallVersionAccessors = new KtorServerCallVersionAccessors(providers, config);
        private final KtorServerConfigVersionAccessors vaccForKtorServerConfigVersionAccessors = new KtorServerConfigVersionAccessors(providers, config);
        private final KtorServerContentVersionAccessors vaccForKtorServerContentVersionAccessors = new KtorServerContentVersionAccessors(providers, config);
        private final KtorServerHostVersionAccessors vaccForKtorServerHostVersionAccessors = new KtorServerHostVersionAccessors(providers, config);
        private final KtorServerHtmlVersionAccessors vaccForKtorServerHtmlVersionAccessors = new KtorServerHtmlVersionAccessors(providers, config);
        private final KtorServerHttpVersionAccessors vaccForKtorServerHttpVersionAccessors = new KtorServerHttpVersionAccessors(providers, config);
        private final KtorServerStatusVersionAccessors vaccForKtorServerStatusVersionAccessors = new KtorServerStatusVersionAccessors(providers, config);
        private final KtorServerTestVersionAccessors vaccForKtorServerTestVersionAccessors = new KtorServerTestVersionAccessors(providers, config);
        public KtorServerVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.auth</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAuth() { return getVersion("ktor.server.auth"); }

        /**
         * Version alias <b>ktor.server.cio</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCio() { return getVersion("ktor.server.cio"); }

        /**
         * Version alias <b>ktor.server.core</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("ktor.server.core"); }

        /**
         * Version alias <b>ktor.server.cors</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCors() { return getVersion("ktor.server.cors"); }

        /**
         * Version alias <b>ktor.server.netty</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNetty() { return getVersion("ktor.server.netty"); }

        /**
         * Version alias <b>ktor.server.websockets</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWebsockets() { return getVersion("ktor.server.websockets"); }

        /**
         * Group of versions at <b>versions.ktor.server.call</b>
         */
        public KtorServerCallVersionAccessors getCall() {
            return vaccForKtorServerCallVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.config</b>
         */
        public KtorServerConfigVersionAccessors getConfig() {
            return vaccForKtorServerConfigVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.content</b>
         */
        public KtorServerContentVersionAccessors getContent() {
            return vaccForKtorServerContentVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.host</b>
         */
        public KtorServerHostVersionAccessors getHost() {
            return vaccForKtorServerHostVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.html</b>
         */
        public KtorServerHtmlVersionAccessors getHtml() {
            return vaccForKtorServerHtmlVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.http</b>
         */
        public KtorServerHttpVersionAccessors getHttp() {
            return vaccForKtorServerHttpVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.status</b>
         */
        public KtorServerStatusVersionAccessors getStatus() {
            return vaccForKtorServerStatusVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.ktor.server.test</b>
         */
        public KtorServerTestVersionAccessors getTest() {
            return vaccForKtorServerTestVersionAccessors;
        }

    }

    public static class KtorServerCallVersionAccessors extends VersionFactory  {

        public KtorServerCallVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.call.id</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getId() { return getVersion("ktor.server.call.id"); }

        /**
         * Version alias <b>ktor.server.call.logging</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLogging() { return getVersion("ktor.server.call.logging"); }

    }

    public static class KtorServerConfigVersionAccessors extends VersionFactory  {

        public KtorServerConfigVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.config.yaml</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getYaml() { return getVersion("ktor.server.config.yaml"); }

    }

    public static class KtorServerContentVersionAccessors extends VersionFactory  {

        public KtorServerContentVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.content.negotiation</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNegotiation() { return getVersion("ktor.server.content.negotiation"); }

    }

    public static class KtorServerHostVersionAccessors extends VersionFactory  {

        public KtorServerHostVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.host.common</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCommon() { return getVersion("ktor.server.host.common"); }

    }

    public static class KtorServerHtmlVersionAccessors extends VersionFactory  {

        public KtorServerHtmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.html.builder</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBuilder() { return getVersion("ktor.server.html.builder"); }

    }

    public static class KtorServerHttpVersionAccessors extends VersionFactory  {

        public KtorServerHttpVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.http.redirect</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRedirect() { return getVersion("ktor.server.http.redirect"); }

    }

    public static class KtorServerStatusVersionAccessors extends VersionFactory  {

        public KtorServerStatusVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.status.pages</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPages() { return getVersion("ktor.server.status.pages"); }

    }

    public static class KtorServerTestVersionAccessors extends VersionFactory  {

        public KtorServerTestVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>ktor.server.test.host</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getHost() { return getVersion("ktor.server.test.host"); }

    }

    public static class LogbackVersionAccessors extends VersionFactory  {

        public LogbackVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>logback.classic</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getClassic() { return getVersion("logback.classic"); }

        /**
         * Version alias <b>logback.version</b> with value <b>1.4.14</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("logback.version"); }

    }

    public static class OpenpdfVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public OpenpdfVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>openpdf</b> with value <b></b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("openpdf"); }

        /**
         * Version alias <b>openpdf.version</b> with value <b>2.0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("openpdf.version"); }

    }

    public static class PostgresVersionAccessors extends VersionFactory  {

        public PostgresVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>postgres.version</b> with value <b>42.7.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getVersion() { return getVersion("postgres.version"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
