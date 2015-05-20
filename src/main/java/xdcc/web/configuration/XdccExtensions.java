package xdcc.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.codestory.http.Request;
import net.codestory.http.Response;
import net.codestory.http.compilers.CompilerFacade;
import net.codestory.http.extensions.Extensions;
import net.codestory.http.io.Resources;
import net.codestory.http.misc.Env;
import net.codestory.http.payload.PayloadWriter;
import net.codestory.http.templating.Site;
import xdcc.model.bot.Bot;
import xdcc.model.bot.BotMetadata;
import xdcc.model.bot.Pack;
import xdcc.web.serializer.BotMetadataSerializer;
import xdcc.web.serializer.BotSerializer;
import xdcc.web.serializer.PackSerializer;

public class XdccExtensions implements Extensions {

  @Override
  public PayloadWriter createPayloadWriter(Request request, Response response, Env env, Site site, Resources resources, CompilerFacade compilers) {
    return new CustomPayloadWriter(request, response, env, site, resources, compilers);
  }

  @Override
  public ObjectMapper configureOrReplaceObjectMapper(ObjectMapper defaultObjectMapper, Env env) {
    SimpleModule simpleModule = new SimpleModule();
    simpleModule
        .addSerializer(Pack.class, new PackSerializer())
        .addSerializer(BotMetadata.class, new BotMetadataSerializer())
        .addSerializer(Bot.class, new BotSerializer());
    defaultObjectMapper.registerModule(simpleModule);
    return defaultObjectMapper;
  }
}
