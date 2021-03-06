package crazypants.enderio.base.config.recipes.xml;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import crazypants.enderio.base.Log;
import crazypants.enderio.base.config.recipes.InvalidRecipeConfigException;
import crazypants.enderio.base.config.recipes.StaxFactory;
import crazypants.enderio.base.integration.tic.TicProxy;

public class Casting extends AbstractCrafting {

  private ItemFloatAmount input;
  private ItemConsumable cast;

  @Override
  public Object readResolve() throws InvalidRecipeConfigException {
    try {
      super.readResolve();
      if (input == null) {
        throw new InvalidRecipeConfigException("Missing <input>");
      }

      valid = valid && input.isValid() && (cast == null ? true : cast.isValid());

    } catch (InvalidRecipeConfigException e) {
      throw new InvalidRecipeConfigException(e, "in <casting>");
    }
    return this;
  }

  @Override
  public void enforceValidity() throws InvalidRecipeConfigException {
    super.enforceValidity();
    input.enforceValidity();
  }

  @Override
  public boolean isActive() {
    return super.isActive();
  }

  @Override
  public void register(@Nonnull String recipeName) {
    if (isValid() && isActive()) {
      if (TicProxy.isLoaded()) {
        TicProxy.registerTableCast(getOutput().getThing(), cast.getThing(), input.getThing(), input.amount, cast.getConsumed());
      } else {
        Log.warn("TiC recipe is active, but TiC integration is not loaded");
      }
    }
  }

  @Override
  public boolean setElement(StaxFactory factory, String name, StartElement startElement) throws InvalidRecipeConfigException, XMLStreamException {
    if ("input".equals(name)) {
      if (input == null) {
        input = factory.read(new ItemFloatAmount().setAllowDelaying(true), startElement);
        return true;
      }
    }
    if ("cast".equals(name)) {
      if (cast == null) {
        cast = factory.read(new ItemConsumable().setAllowDelaying(true), startElement);
        return true;
      }
    }

    return super.setElement(factory, name, startElement);
  }

}